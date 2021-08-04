package br.com.caelum.carangobom.marca;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import br.com.caelum.carangobom.model.entity.Marca;
import br.com.caelum.carangobom.repository.MarcaRepository;


@DataJpaTest
@TestPropertySource(properties = {"DB_NAME=carangobom-test", "spring.jpa.hibernate.ddlAuto:create-drop"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MarcaRepositoryTest {
	
    @Autowired
    private MarcaRepository repository;    
    
    @Test
    void encontraMarcaInformada() {    	
    	var marca = new Marca("Audi");
        repository.save(marca);
        
        var id = marca.getId();
        assertThat(repository.findExistentById(id)).isPresent();
    }
    
    @Test
    void nãoEncontraNenhumaMarcaInformada() {    	
        var id = 4L;
        assertThat(repository.findExistentById(id)).isEmpty();
    }
   
    @Test
    void todasAsMarcasOrganizadasPorNome() {    	
        repository.save(new Marca("Fiat"));
        repository.save(new Marca("Audi"));
        repository.save(new Marca("Jeep"));
        
        List<Marca> response = repository.findAllByOrderByNome();
        assertEquals(3, response.size());
        assertEquals("Audi", response.get(0).getNome());
        assertEquals("Fiat", response.get(1).getNome());
        assertEquals("Jeep", response.get(2).getNome());
    }
}