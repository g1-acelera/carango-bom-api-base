package br.com.caelum.carangobom.autenticacao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import br.com.caelum.carangobom.model.dto.LoginDto;

class LoginDtoTest {
	
	private static final String PREENCHER_DADOS = "Deve ser preenchido.";
	
	private LocalValidatorFactoryBean validator;
	 
	@BeforeEach
    public void configuraMock() {
		validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();
    }
	
    @Test
    void verificarEmailEmBranco() {
    	LoginDto login = new LoginDto();
    	login.setEmail(" ");
    	login.setSenha("senha");
    	
    	Set<ConstraintViolation<LoginDto>> violations = validator.validate(login);
        assertFalse(violations.isEmpty());
        assertEquals(1,violations.size());
        
        for (ConstraintViolation<LoginDto> cv : violations) {
			assertEquals(PREENCHER_DADOS, cv.getMessage());
		}
        
        validator.destroy();        
    }
    
    @Test
    void verificarSenhaEmBranco() {
    	LoginDto login = new LoginDto();
    	login.setEmail("email");
    	
    	Set<ConstraintViolation<LoginDto>> violations = validator.validate(login);
        assertFalse(violations.isEmpty());
        assertEquals(1,violations.size());
        
        for (ConstraintViolation<LoginDto> cv : violations) {
			assertEquals(PREENCHER_DADOS, cv.getMessage());
		}
        
        validator.destroy();     
    }
}