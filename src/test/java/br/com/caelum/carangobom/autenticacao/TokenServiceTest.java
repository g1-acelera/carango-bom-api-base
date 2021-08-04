package br.com.caelum.carangobom.autenticacao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import org.mockito.Mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import br.com.caelum.carangobom.model.dto.LoginDto;
import br.com.caelum.carangobom.model.dto.TokenDto;
import br.com.caelum.carangobom.model.entity.Usuario;
import br.com.caelum.carangobom.service.TokenService;


class TokenServiceTest {
	
	private TokenService tokenService;
	
	@Mock
	public Authentication authentication;
	
	@Mock
	private AuthenticationManager authManager;
	
	
	@BeforeEach
	public void configuraMock() {
		openMocks(this);
		tokenService = new TokenService("8230343", "Teste", authManager);
	}
	
	@Test
	void deveGerarToken() {
		Usuario usuario = new Usuario(1l, "email@teste.com", "senha");
		when(authentication.getPrincipal()).thenReturn(usuario);
		
		String retorno = tokenService.gerarToken(authentication);
		assertNotNull(retorno);
	}
	
	
	@Test
	void verificarTokenInvalido() {
		boolean retorno = tokenService.isTokenValido("Token");
		assertFalse(retorno);
	}
	
	@Test
	void deveRealizarOLogin() {
		LoginDto login = new LoginDto();
		login.setEmail("teste@email.com");
		login.setSenha("senhaTeste");
		
		Authentication auth = Mockito.mock(Authentication.class);
		 Mockito.when(auth.getPrincipal()).thenReturn(new Usuario(1L, "teste@teste", "senha"));
		
		when(authManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
		
		ResponseEntity<TokenDto> resultado = tokenService.realizarLogin(login);
		TokenDto tokenDto = resultado.getBody();
		
        assertNotNull(tokenDto.getToken());
        assertEquals("Bearer", tokenDto.getTipo());
	}
	
	@Test
	void erroAoRealizarOLogin() {
		LoginDto login = new LoginDto();
		login.setEmail("teste@email.com");
		login.setSenha("senhaTeste");
		
		AuthenticationException exception=new DisabledException("Teste");
		
		when(authManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class))).thenThrow(exception);
		ResponseEntity<TokenDto> resultado = tokenService.realizarLogin(login);
		assertNull(resultado.getBody());
	}
	
}
