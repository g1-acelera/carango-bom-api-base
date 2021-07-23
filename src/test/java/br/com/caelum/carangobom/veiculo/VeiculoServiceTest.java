package br.com.caelum.carangobom.veiculo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import br.com.caelum.carangobom.model.dto.VeiculoInputDto;
import br.com.caelum.carangobom.model.dto.VeiculoOutputDto;
import br.com.caelum.carangobom.model.entity.Marca;
import br.com.caelum.carangobom.model.entity.Veiculo;
import br.com.caelum.carangobom.repository.VeiculoRepository;
import br.com.caelum.carangobom.service.VeiculoService;

class VeiculoServiceTest {

	private VeiculoService service;
	private VeiculoInputDto inputDto;
	private List<Veiculo> veiculos;
	
	private static final Marca BMW = new Marca("BMW");
	private static final Marca AUDI = new Marca("Audi");
	private static final Marca FIAT = new Marca("Fiat");

	@Mock
	private VeiculoRepository repository;

	@BeforeEach
	public void configuraMock() {
		openMocks(this);
		service = new VeiculoService(repository);

		inputDto = new VeiculoInputDto();
		inputDto.setNome("Audi");

		veiculos = List.of(
				new Veiculo(1L, "RS6 Avant", 350000.990, 2019, AUDI), 
				new Veiculo(2L, "M3 Competition", 549000.500, 2021, BMW), 
				new Veiculo(3L, "Uno", 8900, 2001, FIAT)
		);
				
	}
	
	@Test
	void deveRetornarListaQuandoHouverResultados() {
		when(repository.findAll()).thenReturn(veiculos);

		List<VeiculoOutputDto> resultado = service.listar();
		assertEquals(3, resultado.size());
		assertEquals("RS6 Avant", resultado.get(0).getNome());
		assertEquals("M3 Competition", resultado.get(1).getNome());
		assertEquals("Uno", resultado.get(2).getNome());
	}
	
	@Test
	void deveRetornarListaVaziaQuandoNaoHouverResultados() {
		List<Veiculo> listaVazia = null;
		
		when(repository.findAll()).thenReturn(listaVazia);

		List<VeiculoOutputDto> resultado = service.listar();
		
		assertEquals(Collections.emptyList(), resultado);
	}
	
	@Test
    void deveCadastrar() {
		when(repository.save(Mockito.any(Veiculo.class)))
			.thenReturn(veiculos.get(0));

        VeiculoOutputDto resposta = service.cadastrar(inputDto);
        
        assertEquals(veiculos.get(0).getId(), resposta.getId());
        assertEquals(veiculos.get(0).getNome(), resposta.getNome());
        assertEquals(veiculos.get(0).getValor(), resposta.getValor());
        assertEquals(veiculos.get(0).getAno(), resposta.getAno());
        assertEquals(veiculos.get(0).getMarca(), resposta.getMarca());
    }
	
	@Test
    void deveAlterarExistente() {
		 when(repository.findById(3L))
	        .thenReturn(Optional.of(veiculos.get(2)));
		
		var veiculoInput = new VeiculoInputDto();
		veiculoInput.setAno(2005);
		veiculoInput.setNome("Unão da massa");
		veiculoInput.setValor(11000);
		veiculoInput.setMarca(FIAT);

        VeiculoOutputDto resposta = service.alterar(3L, veiculoInput).getBody();
        
        assertEquals(2005, resposta.getAno());
        assertEquals(FIAT, resposta.getMarca());
        assertEquals(11000, resposta.getValor());
        assertEquals("Unão da massa", resposta.getNome());
	}
	
	@Test
    void naoDeveAlterarInexistente() {
		 when(repository.findById(4L))
	        .thenReturn(Optional.empty());
		
		var veiculoInput = new VeiculoInputDto();
		veiculoInput.setNome("Unão da massa");

        VeiculoOutputDto resposta = service.alterar(4L, veiculoInput).getBody();
        
        assertNull(resposta);
	}
	
	@Test
    void deveEncontrarEexistentePorId() {
		 when(repository.findById(1L))
	        .thenReturn(Optional.of(veiculos.get(0)));

        VeiculoOutputDto resposta = service.procurarPeloId(1L).getBody();
        
        assertEquals(veiculos.get(0).getId(), resposta.getId());
        assertEquals(veiculos.get(0).getAno(), resposta.getAno());
        assertEquals(veiculos.get(0).getNome(), resposta.getNome());
        assertEquals(veiculos.get(0).getValor(), resposta.getValor());
        assertEquals(veiculos.get(0).getMarca(), resposta.getMarca());
	}
	
	@Test
    void naoDeveEncontrarInexistentePorId() {
		 when(repository.findById(5L))
	        .thenReturn(Optional.empty());

        VeiculoOutputDto resposta = service.procurarPeloId(5L).getBody();
        
        assertNull(resposta);;
	}
	
	@Test
    void deveDeletarExistente() {
		 when(repository.findById(1L))
	        .thenReturn(Optional.of(veiculos.get(0)));

        VeiculoOutputDto resposta = service.deletar(1L).getBody();
        
        assertEquals(veiculos.get(0).getId(), resposta.getId());
        assertEquals(veiculos.get(0).getAno(), resposta.getAno());
        assertEquals(veiculos.get(0).getNome(), resposta.getNome());
        assertEquals(veiculos.get(0).getValor(), resposta.getValor());
        assertEquals(veiculos.get(0).getMarca(), resposta.getMarca());
        
		verify(repository).delete(veiculos.get(0));
	}
	
	@Test
    void naoDeveDeletarInexistente() {
		 when(repository.findById(10L))
	        .thenReturn(Optional.empty());

        VeiculoOutputDto resposta = service.deletar(10L).getBody();
        
        assertNull(resposta);
        verify(repository, never()).delete(any());
	}
}
