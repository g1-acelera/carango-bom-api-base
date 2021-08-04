package br.com.caelum.carangobom.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.caelum.carangobom.model.dto.LoginDto;
import br.com.caelum.carangobom.model.dto.TokenDto;
import br.com.caelum.carangobom.service.TokenService;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {	
	
	@Autowired
	private TokenService tokenService;

	@Autowired
	public AutenticacaoController(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@PostMapping
	@Transactional
	public ResponseEntity<TokenDto> autenticar(@Valid @RequestBody LoginDto loginInformacoes) {
		return tokenService.realizarLogin(loginInformacoes);
	}
}
