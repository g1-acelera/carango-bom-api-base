package br.com.caelum.carangobom.marca;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import javax.validation.ConstraintViolation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import br.com.caelum.carangobom.model.dto.MarcaInputDto;

import static org.assertj.core.api.Assertions.assertThat;

public class MarcaInputDtoTest {
	private static final String MINIMO_TAMANHO_NOME = "Deve ter 2 ou mais caracteres.";
	private static final String PREENCHER_NOME = "Deve ser preenchido.";
	
	private LocalValidatorFactoryBean validator;
	 
	  @BeforeEach
    public void configuraMock() {
		validator = new LocalValidatorFactoryBean();
		validator.afterPropertiesSet();
    }
	
    @Test
    void verificarNomeMinimo() {
    	MarcaInputDto marca = new MarcaInputDto();
    	marca.setNome("a");
    	
    	Set<ConstraintViolation<MarcaInputDto>> violations = validator.validate(marca);
        assertFalse(violations.isEmpty());
        assertTrue(violations.size() == 1);
        
        for (ConstraintViolation<MarcaInputDto> cv : violations) {
			assertEquals(MINIMO_TAMANHO_NOME, cv.getMessage());
		}
        
        validator.destroy();        
    }
    
    @Test
    void verificarNomeBranco() {
    	MarcaInputDto marca = new MarcaInputDto();
    	marca.setNome(" ");
    	
    	Set<ConstraintViolation<MarcaInputDto>> violations = validator.validate(marca);
        assertFalse(violations.isEmpty());
        assertTrue(violations.size() == 2);
        
        for (ConstraintViolation<MarcaInputDto> cv : violations) {
        	String mensagem = cv.getMessage();
        	assertThat(mensagem).matches(actual -> MINIMO_TAMANHO_NOME.equals(actual) || PREENCHER_NOME.equals(actual));
		}
        
        validator.destroy();        
    }
}
