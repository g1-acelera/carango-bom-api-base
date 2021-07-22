package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.model.dto.MarcaInputDto;
import br.com.caelum.carangobom.model.dto.MarcaOutputDto;
import br.com.caelum.carangobom.model.entity.Marca;
import br.com.caelum.carangobom.repository.MarcaRepository;
import br.com.caelum.carangobom.service.MarcaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class MarcaServiceTest {

	private MarcaService marcaService;
	private MarcaInputDto marcaInput;
	private List<Marca> marcas;

	@Mock
	private MarcaRepository marcaRepository;

	@BeforeEach
	public void configuraMock() {
		openMocks(this);
		marcaService = new MarcaService(marcaRepository);

		marcaInput = new MarcaInputDto();
		marcaInput.setNome("Audi");

		marcas = List.of(new Marca(1L, "Audi"), new Marca(2L, "BMW"), new Marca(3L, "Fiat"));
	}

	@Test
	void deveRetornarListaQuandoHouverResultados() {
		when(marcaRepository.findAllByOrderByNome()).thenReturn(marcas);

		List<MarcaOutputDto> resultado = marcaService.listar();
		assertEquals(3, resultado.size());
		assertEquals("Audi", resultado.get(0).getNome());
		assertEquals("BMW", resultado.get(1).getNome());
		assertEquals("Fiat", resultado.get(2).getNome());
	}

	@Test
	void deveRetornarMarcaPeloId() {
		when(marcaRepository.findById(1L)).thenReturn(Optional.of(marcas.get(0)));

		ResponseEntity<MarcaOutputDto> resposta = marcaService.procurarPeloId(1L);
		assertEquals("Audi", resposta.getBody().getNome());
	}
    
    @Test
    void deveCadastrarMarca() {
        when(marcaRepository.save(Mockito.any(Marca.class)))
                .thenReturn(marcas.get(0));

        MarcaOutputDto resposta = marcaService.cadastrar(marcaInput);
        
        assertEquals(marcas.get(0).getId(), resposta.getId());
        assertEquals(marcas.get(0).getNome(), resposta.getNome());
    }
    
    @Test
    void deveAlterarNomeQuandoMarcaExistir() {
        when(marcaRepository.findById(2L))
        .thenReturn(Optional.of(marcas.get(1)));

        ResponseEntity<MarcaOutputDto> resposta = marcaService.alterar(2L, marcaInput);

        MarcaOutputDto marcaAlterada = resposta.getBody();
        assertEquals("Audi", marcaAlterada.getNome());
    }

	@Test
	void naoDeveAlterarMarcaInexistente() {
		when(marcaRepository.findById(anyLong()))
				.thenReturn(Optional.empty());

		ResponseEntity<MarcaOutputDto> resposta = marcaService.alterar(2L, marcaInput);
		assertNull(resposta.getBody());
	}

	@Test
	void deveDeletarMarcaExistente() {
		when(marcaRepository.findById(1L))
				.thenReturn(Optional.of(marcas.get(1)));

		ResponseEntity<MarcaOutputDto> resposta = marcaService.deletar(1L);
		assertEquals(marcas.get(1).getNome(), resposta.getBody().getNome());
		verify(marcaRepository).delete(marcas.get(1));
	}

	@Test
	void naoDeveDeletarMarcaInexistente() {
		when(marcaRepository.findById(anyLong()))
				.thenReturn(Optional.empty());

		ResponseEntity<MarcaOutputDto> resposta = marcaService.deletar(1L);
		assertNull(resposta.getBody());
		verify(marcaRepository, never()).delete(any());
	}
}
