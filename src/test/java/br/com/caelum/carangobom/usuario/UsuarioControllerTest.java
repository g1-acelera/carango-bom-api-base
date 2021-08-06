package br.com.caelum.carangobom.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.caelum.carangobom.controller.UsuarioController;
import br.com.caelum.carangobom.model.dto.UsuarioInputDto;
import br.com.caelum.carangobom.model.dto.UsuarioOutputDto;
import br.com.caelum.carangobom.model.entity.Usuario;
import br.com.caelum.carangobom.service.UsuarioService;

class UsuarioControllerTest {
	
	private UriComponentsBuilder uriBuilder;
	private UsuarioController controller;
	private List<Usuario> usuarios;
	
	@Mock
	private UsuarioService service;
	
	@BeforeEach
	public void setup() {
		openMocks(this);
		controller = new UsuarioController(service);
		uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080");
		
		usuarios = List.of(
				new Usuario(1L, "Usuario 1", "Senha Teste"), 
				new Usuario(2L, "Usuario 2", "Senha Teste 2"),  
				new Usuario(3L, "Usuario 3", "Senha Teste 3"));
	}
	
	@Test
    void deveRetornarListaQuandoHouverDados() {
		List<UsuarioOutputDto> listaEsperada = UsuarioOutputDto.convertToDto(usuarios);
		
		when(service.listar()).thenReturn(listaEsperada);
		
		assertEquals(listaEsperada, controller.lista());
    }
	
	  @Test
	    void deveCadastrar() {	 
		  UsuarioInputDto usuario = new UsuarioInputDto();
		  usuario.setEmail("teste@email.com");
		  usuario.setSenha("testeSenha");
		  
    	when(service.cadastrar(usuario))
    		.thenReturn(ResponseEntity.ok(new UsuarioOutputDto(usuarios.get(0))));
	    	
	    	var resposta = controller.cadastra(usuario, uriBuilder);
	    	
	    	assertEquals(HttpStatus.OK, resposta.getStatusCode());			
	    }
	  
	  @Test
	    void deveDeletarExistente() {	
	        
	    	ResponseEntity<UsuarioOutputDto> usuarioResposta = 
	        		ResponseEntity.ok(new UsuarioOutputDto(usuarios.get(0)));
	        
	        when(service.deletar(1L))
	        	.thenReturn(usuarioResposta);

	        ResponseEntity<UsuarioOutputDto> resposta = controller.deleta(1L);
	        
	        assertEquals(usuarioResposta.getBody(), resposta.getBody());
	        assertEquals(HttpStatus.OK, resposta.getStatusCode());
	    }

	    @Test
	    void naoDeveDeletarInexistente() {
	    	when(service.deletar(2L))
	        	.thenReturn(ResponseEntity.notFound().build());
	        
	        ResponseEntity<UsuarioOutputDto> resposta = controller.deleta(2L);
	        
	        assertNull(resposta.getBody());
	        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
	    }

	
}
