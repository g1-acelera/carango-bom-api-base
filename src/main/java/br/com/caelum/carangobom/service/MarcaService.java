package br.com.caelum.carangobom.service;

import br.com.caelum.carangobom.model.dto.MarcaInputDto;
import br.com.caelum.carangobom.model.dto.MarcaOutputDto;
import br.com.caelum.carangobom.model.entity.Marca;
import br.com.caelum.carangobom.repository.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MarcaService {

	  private MarcaRepository _marcaRepository;

	  @Autowired
	  public MarcaService(MarcaRepository marcaRepository){
	  	this._marcaRepository = marcaRepository;
	  }

	  public List<MarcaOutputDto> listar() {
	  	List<Marca> marcas = this._marcaRepository.findAllByOrderByNome();
		return MarcaOutputDto.convertToDto(marcas);
	  }

	  public ResponseEntity<MarcaOutputDto> procurarPeloId(Long id) {
		  Optional<Marca> marca = _marcaRepository.findById(id);
		  if (marca.isPresent()) {
			  return ResponseEntity.ok(new MarcaOutputDto(marca.get()));
		  } else {
			  return ResponseEntity.notFound().build();
		  }
	  }


	  public MarcaOutputDto cadastrar(MarcaInputDto marcaInput) {
	  	Marca newMarca = new Marca(marcaInput.getNome());
	  	Marca marca = _marcaRepository.save(newMarca);

	  	return new MarcaOutputDto(marca);
	  }
}
