package br.com.caelum.carangobom.veiculo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

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
    void deveSalvarUmNovo() {
        var veiculo = new Veiculo("S4", 240000, 2016, audi);

        assertNull(veiculo.getId());

        repository.save(veiculo);

        assertNotNull(veiculo.getId());

        assertEquals("S4", veiculo.getNome());
        assertEquals(audi, veiculo.getMarca());
    }
    
    @Test
    void deveEncontrarUmExistente() {    	
    	var veiculo = new Veiculo("S4", 240000, 2016, audi);

        repository.save(veiculo);
        
        var id = veiculo.getId();
        
        assertThat(repository.findById(id)).isPresent();
    }
    
    @Test
    void deveDeletarUmExistente() {
    	Optional<Veiculo> veiculoOpcionalVazio = Optional.empty();
    	
    	var veiculo = new Veiculo("S4", 240000, 2016, audi);

        repository.save(veiculo);
        
        var idDoVeiculo = veiculo.getId();
        
        repository.delete(veiculo);

        assertEquals(veiculoOpcionalVazio, repository.findById(idDoVeiculo));
    }
    
    @Test
    void deveListarTodosOsExistentes() {
    	var s4 = new Veiculo("S4", 240000, 2016, audi);
    	var rs6 = new Veiculo("RS6",300000, 2018, audi);
    	var a8 = new Veiculo("A8", 80000, 2014, audi);
    	
    	repository.save(s4);
    	repository.save(rs6);
    	repository.save(a8);
    	
    	var listaDeVeiculos = repository.findAll();
    	
    	assertEquals(List.of(s4, rs6, a8), listaDeVeiculos);
    }

}