package br.com.caelum.carangobom.repository;

import br.com.caelum.carangobom.model.entity.Marca;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MarcaRepository extends CrudRepository<Marca, Long> {
    List<Marca> findAllByOrderByNome();
}
