package br.com.caelum.carangobom.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.caelum.carangobom.model.dto.UsuarioInputDto;
import br.com.caelum.carangobom.model.dto.UsuarioOutputDto;
import br.com.caelum.carangobom.model.entity.Usuario;
import br.com.caelum.carangobom.repository.UsuarioRepository;
import br.com.caelum.carangobom.service.UsuarioService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


class UsuarioServiceTest {
	
	private UsuarioService usuarioService;
	private final String MSG_ERRO_USUARIO = "Dados inválidos!";
	private List<Usuario> usuarios;

	@Mock
	private UsuarioRepository usuarioRepository;

	@BeforeEach
	public void configuraMock() {
		openMocks(this);
		usuarioService = new UsuarioService(usuarioRepository);
		
		usuarios = List.of(
				new Usuario(1L, "Usuario 1", "Senha Teste"), 
				new Usuario(2L, "Usuario 2", "Senha Teste 2"),  
				new Usuario(3L, "Usuario 3", "Senha Teste 3"));
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
	
	@Test
	void deveRetornarListaQuandoHouverResultados() {
		when(usuarioRepository.findAll()).thenReturn(usuarios);

		List<UsuarioOutputDto> resultado = usuarioService.listar();
		assertEquals(3, resultado.size());
		assertEquals("Usuario 1", resultado.get(0).getEmail());
		assertEquals("Usuario 2", resultado.get(1).getEmail());
		assertEquals("Usuario 3", resultado.get(2).getEmail());
	}
	
	@Test
	void deveRetornarListaVaziaQuandoNaoHouverResultados() {
		List<Usuario> listaVazia = null;
		when(usuarioRepository.findAll()).thenReturn(listaVazia);
		
		List<UsuarioOutputDto> resultado = usuarioService.listar();
		
		assertEquals(Collections.emptyList(), resultado);
	}
	
	@Test
	void deveDeletarUsuarioExistente() {
		when(usuarioRepository.findById(1L))
				.thenReturn(Optional.of(usuarios.get(1)));

		ResponseEntity<UsuarioOutputDto> resposta = usuarioService.deletar(1L);
		assertEquals(usuarios.get(1).getEmail(), resposta.getBody().getEmail());
		verify(usuarioRepository).delete(usuarios.get(1));
	}

	@Test
	void naoDeveDeletarMarcaInexistente() {
		when(usuarioRepository.findById(anyLong()))
				.thenReturn(Optional.empty());

		ResponseEntity<UsuarioOutputDto> resposta = usuarioService.deletar(1L);
		assertNull(resposta.getBody());
		verify(usuarioRepository, never()).delete(any());
	}
	
	@Test
    void naoCadastrarSeExisteEmail() {		
		String email = "teste@email.com";
		when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarios.get(0)));
		
		UsuarioInputDto usuarioInput = new UsuarioInputDto();
		
		usuarioInput.setEmail(email);
		usuarioInput.setSenha("teste");

		ResponseEntity<UsuarioOutputDto> resposta = usuarioService.cadastrar(usuarioInput);
        
		assertNull(resposta.getBody());
    }
	
	@Test
    void deveCadastrarUsuario() {		
		String email = "Usuario 1";
		when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());
		when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuarios.get(0));
		
		UsuarioInputDto usuarioInput = new UsuarioInputDto();
		
		usuarioInput.setEmail(email);
		usuarioInput.setSenha("teste");

		ResponseEntity<UsuarioOutputDto> resposta = usuarioService.cadastrar(usuarioInput);
        
		assertNotNull(resposta.getBody());
		assertEquals(email, resposta.getBody().getEmail());
		assertNotNull(resposta.getBody().getId());
    }

}
