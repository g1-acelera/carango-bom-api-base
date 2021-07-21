package br.com.caelum.carangobom.service;

import br.com.caelum.carangobom.model.dto.MarcaInputDto;
import br.com.caelum.carangobom.model.dto.MarcaOutputDto;
import br.com.caelum.carangobom.model.entity.Marca;
import br.com.caelum.carangobom.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaService {

	private MarcaRepository marcaRepository;

	@Autowired
	public MarcaService(MarcaRepository marcaRepository) {
		this.marcaRepository = marcaRepository;
	}

	public List<MarcaOutputDto> listar() {
		List<Marca> marcas = this.marcaRepository.findAllByOrderByNome();
		return MarcaOutputDto.convertToDto(marcas);
	}

	public ResponseEntity<MarcaOutputDto> procurarPeloId(Long id) {
		Optional<Marca> marca = marcaRepository.findById(id);

		if (marca.isPresent()) {
			return ResponseEntity.ok(new MarcaOutputDto(marca.get()));
		}

		return ResponseEntity.notFound().build();
	}

	public MarcaOutputDto cadastrar(MarcaInputDto marcaInput) {
		Marca newMarca = new Marca(marcaInput.getNome());
		Marca marca = marcaRepository.save(newMarca);

		return new MarcaOutputDto(marca);
	}

	public ResponseEntity<MarcaOutputDto> alterar(Long id, MarcaInputDto marcaInput) {
		Optional<Marca> marca = marcaRepository.findById(id);

		if (marca.isPresent()) {
			Marca marcaAtualizada = marca.get();
			marcaAtualizada.setNome(marcaInput.getNome());

			return ResponseEntity.ok(new MarcaOutputDto(marcaAtualizada));
		}

		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<MarcaOutputDto> deletar(Long id) {
		Optional<Marca> marca = marcaRepository.findById(id);

		if (marca.isPresent()) {
			Marca marcaDeletar = marca.get();
			marcaRepository.delete(marcaDeletar);

			return ResponseEntity.ok(new MarcaOutputDto(marcaDeletar));
		}

		return ResponseEntity.notFound().build();
	}

}
