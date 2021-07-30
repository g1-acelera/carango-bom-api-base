package br.com.caelum.carangobom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caelum.carangobom.model.entity.Veiculo;

import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, String> {
    Optional<Veiculo> findById(Long id);
}
