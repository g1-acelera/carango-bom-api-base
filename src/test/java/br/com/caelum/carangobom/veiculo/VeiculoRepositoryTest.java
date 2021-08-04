package br.com.caelum.carangobom.veiculo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import br.com.caelum.carangobom.model.entity.Marca;
import br.com.caelum.carangobom.model.entity.Veiculo;
import br.com.caelum.carangobom.repository.VeiculoRepository;

@DataJpaTest
@TestPropertySource(properties = {"DB_NAME=carangobom-test", "spring.jpa.hibernate.ddlAuto:create-drop"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VeiculoRepositoryTest {
	
	private Marca audi;

    @Autowired
    private VeiculoRepository repository;

    @Autowired
    private TestEntityManager entityManager;
    
    @BeforeEach
    void setup() {
    	audi = new Marca("Audi");
    	entityManager.persist(audi);
    }
    
    @Test
    void deveEncontrarUmExistente() {    	
    	var veiculo = new Veiculo("S4", 240000, 2016, audi);
        repository.save(veiculo);
        
        var id = veiculo.getId();
        
        assertThat(repository.findById(id)).isPresent();
    }
    
    @Test
    void nãoEncontraNenhumVeiculoExistente() {    	
    	var veiculo = new Veiculo("S4", 240000, 2016, audi);
        repository.save(veiculo);
        
        var id = 4L;
        
        assertThat(repository.findById(id)).isEmpty();
    }
   
}