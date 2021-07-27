package br.com.caelum.carangobom.repository;

import org.springframework.data.repository.Repository;

import br.com.caelum.carangobom.model.entity.Veiculo;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends Repository<Veiculo, String> {
    Optional<Veiculo> findById(Long id);

    List<Veiculo> findAll();

    Veiculo save(Veiculo veiculo);

    void delete(Veiculo veiculo);
}
