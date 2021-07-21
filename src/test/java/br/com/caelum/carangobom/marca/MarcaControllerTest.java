package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.controller.MarcaController;
import br.com.caelum.carangobom.model.dto.MarcaInputDto;
import br.com.caelum.carangobom.model.dto.MarcaOutputDto;
import br.com.caelum.carangobom.model.entity.Marca;
import br.com.caelum.carangobom.service.MarcaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class MarcaControllerTest {

    private MarcaController _marcaController;
    private UriComponentsBuilder uriBuilder;
    private MarcaInputDto marcaInput;
    private List<Marca> marcas;
    private static final String urlLocal = "http://localhost:8080/marcas/1";

    @Mock
    private MarcaService _marcaService;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);
        _marcaController = new MarcaController(_marcaService);
        uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080");
        
        marcaInput = new MarcaInputDto();
    	marcaInput.setNome("Ferrari");
    	
    	marcas = List.of(
			new Marca(1L, "Audi"),
	        new Marca(2L, "BMW"),
	        new Marca(3L, "Fiat")
	    );
    }

    @Test
    void deveRetornarListaQuandoHouverResultados() {
        List<MarcaOutputDto> marcasDto = MarcaOutputDto.convertToDto(marcas);

        when(_marcaService.listar())
            .thenReturn(marcasDto);

        List<MarcaOutputDto> resultado = _marcaController.lista();
        assertEquals(marcasDto, resultado);
    }

    @Test
    void deveRetornarMarcaPeloId() {
        MarcaOutputDto marcaOutput = new MarcaOutputDto(marcas.get(0));
        ResponseEntity<MarcaOutputDto> marcaResponse = ResponseEntity.ok(marcaOutput);

        when(_marcaService.procurarPeloId(1L))
            .thenReturn(marcaResponse);

        ResponseEntity<MarcaOutputDto> resposta = _marcaController.procuraPeloId(1L);
        assertEquals(marcaOutput, resposta.getBody());
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void deveRetornarNotFoundQuandoRecuperarMarcaComIdInexistente() {
        when(_marcaService.procurarPeloId(anyLong()))
                .thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<MarcaOutputDto> resposta = _marcaController.procuraPeloId(1L);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveResponderCreatedELocationQuandoCadastrarMarca() {
        when(_marcaService.cadastrar(marcaInput))
                .thenReturn(new MarcaOutputDto(new Marca(1L, marcaInput.getNome())));

        ResponseEntity<MarcaOutputDto> resposta = _marcaController.cadastra(marcaInput, uriBuilder);
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(urlLocal, resposta.getHeaders().getLocation().toString());
    }

    @Test
    void deveResponderValidationFailedQuandoCadastrarMarcaComNomeVazio() {
        assertThrows(NullPointerException.class, () -> {
            _marcaController.cadastra(new MarcaInputDto(), uriBuilder);
        });
    }

    @Test
    void deveAlterarNomeQuandoMarcaExistir() {
        Marca marca = new Marca(1L, marcaInput.getNome());
        
        ResponseEntity<MarcaOutputDto> marcaResponse = ResponseEntity.ok(new MarcaOutputDto(marca));
        
        when(_marcaService.alterar(1L, marcaInput))
        .thenReturn(marcaResponse);

        ResponseEntity<MarcaOutputDto> resposta = _marcaController.altera(1L, marcaInput);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());

        MarcaOutputDto marcaAlterada = resposta.getBody();
        assertEquals("Ferrari", marcaAlterada.getNome());
    }

    @Test
    void naoDeveAlterarMarcaInexistente() {    	
        when(_marcaService.alterar(2L, marcaInput))
        .thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<MarcaOutputDto> resposta = _marcaController.altera(2L, marcaInput);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }


    @Test
    void deveDeletarMarcaExistente() {
        Marca marca = new Marca(1L, marcaInput.getNome());
        
        ResponseEntity<MarcaOutputDto> marcaResponse = ResponseEntity.ok(new MarcaOutputDto(marca));
        
        when(_marcaService.deletar(1L))
        .thenReturn(marcaResponse);

        ResponseEntity<MarcaOutputDto> resposta = _marcaController.deleta(1L);
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void naoDeveDeletarMarcaInexistente() {    	
        when(_marcaService.deletar(2L))
        .thenReturn(ResponseEntity.notFound().build());
        
        ResponseEntity<MarcaOutputDto> resposta = _marcaController.deleta(2L);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

}