package com.cassioroos.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cassioroos.cursomc.DTO.ClienteDTO;
import com.cassioroos.cursomc.DTO.ClienteNewDTO;
import com.cassioroos.cursomc.domain.Cidade;
import com.cassioroos.cursomc.domain.Cliente;
import com.cassioroos.cursomc.domain.Endereco;
import com.cassioroos.cursomc.domain.enums.Perfil;
import com.cassioroos.cursomc.domain.enums.TipoCliente;
import com.cassioroos.cursomc.repositories.CidadeRepository;
import com.cassioroos.cursomc.repositories.ClienteRepository;
import com.cassioroos.cursomc.repositories.EnderecoRepository;
import com.cassioroos.cursomc.security.UserSS;
import com.cassioroos.cursomc.services.exceptions.AuthorizationException;
import com.cassioroos.cursomc.services.exceptions.DataIntegrityException;
import com.cassioroos.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	@Autowired
	private CidadeRepository repoCidade;

	@Autowired
	private EnderecoRepository repoEndereco;

	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private S3Service s3Service;

	@Autowired
	private ImageService imageService;

	@Value("${img.profile.size}")
	private Integer size;

	public Cliente find(Integer id) {
		UserSS user = UserService.authenticated();
		if (user == null || !user.hasRole(Perfil.ADMIN) && !user.getId().equals(id)) {
			throw new AuthorizationException("Acesso negado!");
		}

		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! id : " + id + " tipo: " + Cliente.class.getName()));
	}

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		repoEndereco.saveAll(obj.getEnderecos());
		return obj;
	}

	public List<Cliente> findAll() {
		return repo.findAll();
	}

	public Cliente findByEmail(String email) {
		UserSS user = UserService.authenticatedEmailUser(email);

		Cliente cli = findByEmail(email);
		if (cli == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado: " + user.getId() + " tipo " + Cliente.class.getName());
		}

		return cli;
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public void delete(Integer cod) {
		Cliente obj = find(cod);
		try {
			repo.delete(obj);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível deletar pois há entidades relaciondas a esse cliente.");
		}
	}

	public Cliente fromDTO(ClienteDTO obj) {
		return new Cliente(obj.getId(), obj.getNome(), obj.getEmail(), null, null, null);
	}

	public Cliente fromDTO(ClienteNewDTO obj) {
		Cliente cli = new Cliente(null, obj.getNome(), obj.getEmail(), obj.getCpfOuCnpj(),
				TipoCliente.toEnum(obj.getTipo()), pe.encode(obj.getSenha()));
		Cidade cid = repoCidade.findById(obj.getCidadeid())
				.orElseThrow(() -> new ObjectNotFoundException("Cidade não encontrada! Código " + obj.getCidadeid()));
		Endereco end = new Endereco(null, obj.getLogradouro(), obj.getNumero(), obj.getComplemento(), obj.getBairro(),
				obj.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(obj.getTelefone1());

		if (obj.getTelefone2() != null) {
			cli.getTelefones().add(obj.getTelefone2());
		}

		if (obj.getTelefone3() != null) {
			cli.getTelefones().add(obj.getTelefone3());
		}
		return cli;
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticatedUser();

		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		String fileName = "profilepicture" + user.getId() + ".jpg";
		jpgImage = imageService.cropSquare(jpgImage);
		jpgImage = imageService.resize(jpgImage, size);

		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), "image", fileName);
	}

}
