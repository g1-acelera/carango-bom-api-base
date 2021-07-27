package br.com.caelum.carangobom.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.caelum.carangobom.exception.MarcaNotFoundException;
import br.com.caelum.carangobom.model.dto.VeiculoInputDto;
import br.com.caelum.carangobom.model.dto.VeiculoOutputDto;
import br.com.caelum.carangobom.model.entity.Marca;
import br.com.caelum.carangobom.model.entity.Veiculo;
import br.com.caelum.carangobom.repository.VeiculoRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VeiculoService {

    private final VeiculoRepository repository;
    private final ExistentMarcaService marcaService;

    public ResponseEntity<VeiculoOutputDto> procurarPeloId(Long id) {
		Optional<Veiculo> veiculo = repository.findById(id);

		if (veiculo.isPresent()) {
			return ResponseEntity.ok(new VeiculoOutputDto(veiculo.get()));
		}

		return ResponseEntity.notFound().build();
	}
    
    public List<VeiculoOutputDto> listar() {
		List<Veiculo> veiculos = this.repository.findAll();
		try {
			return VeiculoOutputDto.convertToDto(veiculos);
		}
		catch (NullPointerException e) {
			return Collections.emptyList();
		}
	}

    public VeiculoOutputDto cadastrar(VeiculoInputDto veiculoInput) {
    	var marca = buscarMarca(veiculoInput.getMarcaId());
    	
		Veiculo newVeiculo = new Veiculo(
				veiculoInput.getNome(), 
				veiculoInput.getValor(), 
				veiculoInput.getAno(), 
				marca
		);
		
		newVeiculo = this.repository.save(newVeiculo);

		return new VeiculoOutputDto(newVeiculo);
	}

	public ResponseEntity<VeiculoOutputDto> alterar(Long id, VeiculoInputDto veiculoInput) {
		Optional<Veiculo> veiculo = this.repository.findById(id);

		if (veiculo.isPresent()) {
			var marca = buscarMarca(veiculoInput.getMarcaId());
			var veiculoAtualizado = veiculo.get();
			veiculoAtualizado.setAno(veiculoInput.getAno());
			veiculoAtualizado.setNome(veiculoInput.getNome());
			veiculoAtualizado.setValor(veiculoInput.getValor());
			veiculoAtualizado.setMarca(marca);

			return ResponseEntity.ok(new VeiculoOutputDto(veiculoAtualizado));
		}

		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<VeiculoOutputDto> deletar(Long id) {
		Optional<Veiculo> veiculo = this.repository.findById(id);

		if (veiculo.isPresent()) {
			Veiculo veiculoDeletado = veiculo.get();
			this.repository.delete(veiculoDeletado);

			return ResponseEntity.ok(new VeiculoOutputDto(veiculoDeletado));
		}

		return ResponseEntity.notFound().build();
	}
	
	private Marca buscarMarca(Long marcaId) {
		return marcaService.findExistentById(marcaId).
				orElseThrow(() -> new MarcaNotFoundException("ID de Marca é inválido"));
	}
    
}
