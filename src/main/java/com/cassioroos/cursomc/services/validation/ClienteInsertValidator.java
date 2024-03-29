package com.cassioroos.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.cassioroos.cursomc.DTO.ClienteNewDTO;
import com.cassioroos.cursomc.domain.Cliente;
import com.cassioroos.cursomc.domain.enums.TipoCliente;
import com.cassioroos.cursomc.repositories.ClienteRepository;
import com.cassioroos.cursomc.resources.exceptions.FieldMessage;
import com.cassioroos.cursomc.services.validation.util.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {
	}

	
	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();

		if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && ! BR.isValidCPF(objDto.getCpfOuCnpj())){
			list.add(new FieldMessage("CPF inválido", "cpfOuCnpj"));
		}
		if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && ! BR.isValidCNPJ(objDto.getCpfOuCnpj())){
			list.add(new FieldMessage("CNPJ inválido", "cpfOuCnpj"));
		}
		
		Cliente cli = repo.findByEmail(objDto.getEmail());
		
		if (cli != null) {
			list.add(new FieldMessage("Email já existente", "email"));
		}
		
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}