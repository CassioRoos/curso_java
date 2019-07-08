package com.cassioroos.cursomc.DTO;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import com.cassioroos.cursomc.domain.Categoria;

import lombok.Getter;
import lombok.Setter;

@Data
public class CategoriaDTO  implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;

	@NotEmpty(message = "Preenchimento obrigatório")
	@Length(min = 5, max = 80, message = "Tamanho deve ser entre 5 e 80")
	private String nome;
	
	public CategoriaDTO(Categoria obj) {
		id = obj.getId();
		nome = obj.getNome();
	}
}
