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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class MarcaServiceTest {

	private MarcaService _marcaService;
	private UriComponentsBuilder uriBuilder;
	private MarcaInputDto marcaInput;
	private List<Marca> marcas;
	private static final String urlLocal = "http://localhost:8080/marcas/1";

	@Mock
	private MarcaRepository _marcaRepository;

	@BeforeEach
	public void configuraMock() {
		openMocks(this);
		_marcaService = new MarcaService(_marcaRepository);
		uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080");

		marcaInput = new MarcaInputDto();
		marcaInput.setNome("Audi");

		marcas = List.of(new Marca(1L, "Audi"), new Marca(2L, "BMW"), new Marca(3L, "Fiat"));
	}

	@Test
	void deveRetornarListaQuandoHouverResultados() {
		when(_marcaRepository.findAllByOrderByNome()).thenReturn(marcas);

		List<MarcaOutputDto> resultado = _marcaService.listar();
		assertEquals(3, resultado.size());
		assertEquals("Audi", resultado.get(0).getNome());
		assertEquals("BMW", resultado.get(1).getNome());
		assertEquals("Fiat", resultado.get(2).getNome());
	}

	@Test
	void deveRetornarMarcaPeloId() {
		when(_marcaRepository.findById(1L)).thenReturn(Optional.of(marcas.get(0)));

		ResponseEntity<MarcaOutputDto> resposta = _marcaService.procurarPeloId(1L);
		assertEquals("Audi", resposta.getBody().getNome());
	}
    
    @Test
    void deveCadastrarMarca() {
        when(_marcaRepository.save(Mockito.any(Marca.class)))
                .thenReturn(marcas.get(0));

        MarcaOutputDto resposta = _marcaService.cadastrar(marcaInput);
        
        assertEquals(marcas.get(0).getId(), resposta.getId());
        assertEquals(marcas.get(0).getNome(), resposta.getNome());
    }
    
    @Test
    void deveAlterarNomeQuandoMarcaExistir() {
        when(_marcaRepository.findById(2L))
        .thenReturn(Optional.of(marcas.get(1)));

        ResponseEntity<MarcaOutputDto> resposta = _marcaService.alterar(2L, marcaInput);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());

        MarcaOutputDto marcaAlterada = resposta.getBody();
        assertEquals("Audi", marcaAlterada.getNome());
    }

	@Test
	void naoDeveAlterarMarcaInexistente() {
		when(_marcaRepository.findById(anyLong()))
				.thenReturn(Optional.empty());

		ResponseEntity<MarcaOutputDto> resposta = _marcaService.alterar(2L, marcaInput);
		assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
	}

	@Test
	void deveDeletarMarcaExistente() {
		when(_marcaRepository.findById(1L))
				.thenReturn(Optional.of(marcas.get(1)));

		ResponseEntity<MarcaOutputDto> resposta = _marcaService.deletar(1L);
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		verify(_marcaRepository).delete(marcas.get(1));
	}

	@Test
	void naoDeveDeletarMarcaInexistente() {
		when(_marcaRepository.findById(anyLong()))
				.thenReturn(Optional.empty());

		ResponseEntity<MarcaOutputDto> resposta = _marcaService.deletar(1L);
		assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
		verify(_marcaRepository, never()).delete(any());
	}
}
