package br.com.caelum.carangobom.veiculo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
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

import br.com.caelum.carangobom.exception.MarcaNotFoundException;
import br.com.caelum.carangobom.model.dto.VeiculoInputDto;
import br.com.caelum.carangobom.model.dto.VeiculoOutputDto;
import br.com.caelum.carangobom.model.entity.Marca;
import br.com.caelum.carangobom.model.entity.Veiculo;
import br.com.caelum.carangobom.repository.VeiculoRepository;
import br.com.caelum.carangobom.service.ExistentMarcaService;
import br.com.caelum.carangobom.service.VeiculoService;

class VeiculoServiceTest {

	private VeiculoService service;
	private List<Veiculo> veiculos;
	private static VeiculoInputDto inputDto;
	
	private static final Marca BMW = new Marca(1L, "BMW");
	private static final Marca AUDI = new Marca(2L, "Audi");
	private static final Marca FIAT = new Marca(3L, "Fiat");

	@Mock
	private VeiculoRepository repository;
	
	@Mock
	private ExistentMarcaService marcaService;

	@BeforeEach
	public void setup() {
		openMocks(this);
		service = new VeiculoService(repository, marcaService);
		
		inputDto = new VeiculoInputDto();
		
		var rs6 = new Veiculo();
		rs6.setId(1L);
		rs6.setNome("RS6 Avant");
		rs6.setValor(350000.990);
		rs6.setAno(2019);
		rs6.setMarca(AUDI);

		veiculos = List.of(
				rs6, 
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
		when(marcaService.findById(3L))
			.thenReturn(Optional.of(FIAT));
		
		when(repository.save(Mockito.any(Veiculo.class)))
			.thenReturn(veiculos.get(0));
		
		inputDto.setNome("Palio C/ Pintura Queimada");
		inputDto.setValor(5000);
		inputDto.setAno(1998);
		inputDto.setMarcaId(3L);

        VeiculoOutputDto resposta = service.cadastrar(inputDto);
        
        assertEquals(veiculos.get(0).getId(), resposta.getId());
        assertEquals(veiculos.get(0).getNome(), resposta.getNome());
        assertEquals(veiculos.get(0).getValor(), resposta.getValor());
        assertEquals(veiculos.get(0).getAno(), resposta.getAno());
        assertEquals(veiculos.get(0).getMarca(), resposta.getMarca());
    }
	
	@Test
    void deveAlterarExistente() {
		when(marcaService.findById(3L))
			.thenReturn(Optional.of(FIAT));
		 when(repository.findById(3L))
	        .thenReturn(Optional.of(veiculos.get(2)));
		
		inputDto.setAno(2005);
		inputDto.setNome("Unão da massa");
		inputDto.setValor(11000);
		inputDto.setMarcaId(FIAT.getId());

        VeiculoOutputDto resposta = service.alterar(3L, inputDto).getBody();
        
        assertEquals(2005, resposta.getAno());
        assertEquals(FIAT, resposta.getMarca());
        assertEquals(11000, resposta.getValor());
        assertEquals("Unão da massa", resposta.getNome());
	}
	
	@Test
    void naoDeveAlterarInexistente() {
		when(repository.findById(4L))
	    	.thenReturn(Optional.empty());
		
		inputDto.setNome("Unão da massa");
		inputDto.setNome("Uno Mille EP");
		inputDto.setValor(6500);
		inputDto.setMarcaId(12L);

        VeiculoOutputDto resposta = service.alterar(4L, inputDto).getBody();
        
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
	
	@Test
    void deveJogarExceptionAoTentarAlterarVeiculoComMarcaInexistente() {
	    when(repository.findById(1L))
	    	.thenReturn(Optional.of(veiculos.get(0)));
		when(marcaService.findById(12L))
	    	.thenReturn(Optional.empty());
		
		inputDto.setAno(1998);
		inputDto.setNome("Uno Mille EP");
		inputDto.setValor(6500);
		inputDto.setMarcaId(12L);
        
        assertThrows(MarcaNotFoundException.class, () -> service.alterar(1L, inputDto).getBody());
        
        then(repository).should(only()).findById(1L);
        then(marcaService).should(only()).findById(12L);
	}
}
