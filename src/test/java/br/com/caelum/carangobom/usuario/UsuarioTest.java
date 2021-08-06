package br.com.caelum.carangobom.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.caelum.carangobom.model.dto.UsuarioOutputDto;
import br.com.caelum.carangobom.model.entity.Usuario;

class UsuarioTest {
		
	@BeforeEach
	public void setup() {
		
	}
	
	
	@Test
    void verificarRetornoSenha() {
		Usuario usuario = new Usuario(1L, "teste@email.com", "teste");
		assertEquals(usuario.getSenha(), usuario.getPassword());
    }
	
	@Test
    void verificarRetornoEmail() {
		Usuario usuario = new Usuario(1L, "teste@email.com", "teste");
		assertEquals(usuario.getEmail(), usuario.getUsername());
    }
	
	@Test
    void verificarMetodosOverride() {
		Usuario usuario = new Usuario(1L, "teste@email.com", "teste");
		assertTrue(usuario.isAccountNonExpired());
		assertTrue(usuario.isAccountNonLocked());
		assertTrue(usuario.isCredentialsNonExpired());
		assertTrue(usuario.isEnabled());
    }
	
	
}
