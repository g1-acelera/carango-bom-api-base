package br.com.caelum.carangobom.autenticacao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.caelum.carangobom.controller.AutenticacaoController;
import br.com.caelum.carangobom.model.dto.LoginDto;
import br.com.caelum.carangobom.model.dto.TokenDto;
import br.com.caelum.carangobom.service.TokenService;

class AutenticacaoControllerTest {
	
	private AutenticacaoController autenticacaoController;
	
	@Mock
	private TokenService tokenService;
	
	
	@BeforeEach
	public void setup() {
		openMocks(this);
		autenticacaoController = new AutenticacaoController(tokenService);
	}
	
	@Test
    void sucessoAoAtenticar() {
		LoginDto loginDto = new LoginDto();
		loginDto.setEmail("teste@email.com");
		loginDto.setSenha("senha");
		
		TokenDto token = new TokenDto("TokenTest", "Bearer");
		
		ResponseEntity<TokenDto> marcaResponse = ResponseEntity.ok(token);
		when(tokenService.realizarLogin(Mockito.any(LoginDto.class))).thenReturn(marcaResponse);
		
		ResponseEntity<TokenDto> resposta = autenticacaoController.autenticar(loginDto);
		assertEquals(token, resposta.getBody());
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }
	
	@Test
    void erroAoAtenticar() {
		LoginDto loginDto = new LoginDto();
		loginDto.setEmail("teste@email.com");
		loginDto.setSenha("senha");
				
		when(tokenService.realizarLogin(Mockito.any(LoginDto.class))).thenReturn(ResponseEntity.notFound().build());
		
		ResponseEntity<TokenDto> resposta = autenticacaoController.autenticar(loginDto);
		assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }
}
