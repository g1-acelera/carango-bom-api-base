package br.com.caelum.carangobom.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.caelum.carangobom.model.entity.Usuario;

public class UsuarioOutputDto {
	  	private Long id;
	    private String email;

	    public UsuarioOutputDto(Usuario usuario) {
	        this.id = usuario.getId();
	        this.email = usuario.getEmail();
	    }

	    public Long getId() {
	        return id;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public static List<UsuarioOutputDto> convertToDto(List<Usuario> usuarios) {
	        return usuarios.stream().map(UsuarioOutputDto::new).collect(Collectors.toList());
	    }
}
