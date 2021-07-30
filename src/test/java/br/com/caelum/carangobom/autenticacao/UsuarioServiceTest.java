package br.com.caelum.carangobom.autenticacao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.caelum.carangobom.model.entity.Usuario;
import br.com.caelum.carangobom.repository.UsuarioRepository;
import br.com.caelum.carangobom.service.UsuarioService;

import java.util.Optional;


class UsuarioServiceTest {
	
	private UsuarioService usuarioService;
	private final String MSG_ERRO_USUARIO = "Dados inválidos!";

	@Mock
	private UsuarioRepository usuarioRepository;

	@BeforeEach
	public void configuraMock() {
		openMocks(this);
		usuarioService = new UsuarioService(usuarioRepository);
	}

	@Test
	void deveRetornarUsuarioQuandoDadosCorretos() {
		String email = "teste@email.com";
		Usuario usuario = new Usuario(email, "senha");
		when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

		UserDetails resultado = usuarioService.loadUserByUsername(email);
		assertEquals(email, resultado.getUsername());
	}
	
	@Test
	void deveRetornarErroQuandoDadosDoUsuarioIncorreto() {
		String email = "teste@email.com";
		when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());
		
		 Throwable exception = assertThrows(UsernameNotFoundException.class, () -> usuarioService.loadUserByUsername(email));
		 assertEquals(MSG_ERRO_USUARIO, exception.getMessage());
	}

}
