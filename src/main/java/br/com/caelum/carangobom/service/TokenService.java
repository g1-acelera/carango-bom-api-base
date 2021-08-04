package br.com.caelum.carangobom.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import br.com.caelum.carangobom.model.dto.LoginDto;
import br.com.caelum.carangobom.model.dto.TokenDto;
import br.com.caelum.carangobom.model.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class TokenService {
	
	private static final String TIPOTOKEN = "Bearer";
	
	@Value("${jwt.expiration}")
	private String expiration;
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Autowired
	private AuthenticationManager authManager;

	public String gerarToken(Authentication authentication) {
		Usuario logado = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		
		return Jwts.builder()
				.setIssuer("API venda de veiculos")
				.setSubject(logado.getId().toString())
				.setIssuedAt(hoje)
				.setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}
	
	public boolean isTokenValido(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Long getIdUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}
	
	public ResponseEntity<TokenDto> realizarLogin(LoginDto loginInformacoes){
		UsernamePasswordAuthenticationToken dadosLogin = loginInformacoes.converter();
	
		try {
			Authentication authentication = authManager.authenticate(dadosLogin);
			String token = gerarToken(authentication);
			return ResponseEntity.ok(new TokenDto(token, TIPOTOKEN));
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
