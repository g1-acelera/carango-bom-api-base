package br.com.caelum.carangobom.service;

import java.util.Optional;

import br.com.caelum.carangobom.model.entity.Marca;

public interface ExistentMarcaService {
	Optional<Marca> findExistentById(Long id);
}
