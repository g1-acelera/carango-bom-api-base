package br.com.caelum.carangobom.marca;

import br.com.caelum.carangobom.controller.MarcaController;
import br.com.caelum.carangobom.model.dto.MarcaInputDto;
import br.com.caelum.carangobom.model.dto.MarcaOutputDto;
import br.com.caelum.carangobom.model.entity.Marca;
import br.com.caelum.carangobom.repository.MarcaRepository;
import br.com.caelum.carangobom.service.MarcaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class MarcaControllerTest {

    private MarcaController _marcaController;
    private UriComponentsBuilder uriBuilder;

    @Mock
    private MarcaService _marcaService;

    @BeforeEach
    public void configuraMock() {
        openMocks(this);
        _marcaController = new MarcaController(_marcaService);
        uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080");
    }

    @Test
    void deveRetornarListaQuandoHouverResultados() {
        List<Marca> marcas = List.of(
            new Marca(1L, "Audi"),
            new Marca(2L, "BMW"),
            new Marca(3L, "Fiat")
        );

        List<MarcaOutputDto> marcasDto = MarcaOutputDto.convertToDto(marcas);

        when(_marcaService.listar())
            .thenReturn(marcasDto);

        List<MarcaOutputDto> resultado = _marcaController.listar();
        assertEquals(marcasDto, resultado);
    }

    @Test
    void deveRetornarMarcaPeloId() {
        Marca audi = new Marca(1L, "Audi");

        MarcaOutputDto audiDto = new MarcaOutputDto(audi);
        ResponseEntity<MarcaOutputDto> audiRe = ResponseEntity.ok(audiDto);

        when(_marcaService.procurarPeloId(1L))
            .thenReturn(audiRe);

        ResponseEntity<MarcaOutputDto> resposta = _marcaController.procurarPeloId(1L);
        assertEquals(audiDto, resposta.getBody());
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void deveRetornarNotFoundQuandoRecuperarMarcaComIdInexistente() {
        when(_marcaService.procurarPeloId(anyLong()))
                .thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<MarcaOutputDto> resposta = _marcaController.procurarPeloId(1L);
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveResponderCreatedELocationQuandoCadastrarMarca() {
        MarcaInputDto ferrari = new MarcaInputDto();
        ferrari.setNome("Ferrari");

        when(_marcaService.cadastrar(ferrari))
                .thenReturn(new MarcaOutputDto(new Marca(1L, ferrari.getNome())));

        ResponseEntity<MarcaOutputDto> resposta = _marcaController.cadastrar(ferrari, uriBuilder);
        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals("http://localhost:8080/marcas/1", resposta.getHeaders().getLocation().toString());
    }

//    @Test
//    void deveAlterarNomeQuandoMarcaExistir() {
//        Marca audi = new Marca(1L, "Audi");
//
//        when(marcaRepository.findById(1L))
//            .thenReturn(Optional.of(audi));
//
//        ResponseEntity<Marca> resposta = _marcaController.altera(1L, new Marca(1L, "NOVA Audi"));
//        assertEquals(HttpStatus.OK, resposta.getStatusCode());
//
//        Marca marcaAlterada = resposta.getBody();
//        assertEquals("NOVA Audi", marcaAlterada.getNome());
//    }
//
//    @Test
//    void naoDeveAlterarMarcaInexistente() {
//        when(marcaRepository.findById(anyLong()))
//                .thenReturn(Optional.empty());
//
//        ResponseEntity<Marca> resposta = _marcaController.altera(1L, new Marca(1L, "NOVA Audi"));
//        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
//    }
//
//    @Test
//    void deveDeletarMarcaExistente() {
//        Marca audi = new Marca(1l, "Audi");
//
//        when(marcaRepository.findById(1L))
//            .thenReturn(Optional.of(audi));
//
//        ResponseEntity<Marca> resposta = _marcaController.deleta(1L);
//        assertEquals(HttpStatus.OK, resposta.getStatusCode());
//        verify(marcaRepository).delete(audi);
//    }
//
//    @Test
//    void naoDeveDeletarMarcaInexistente() {
//        when(marcaRepository.findById(anyLong()))
//                .thenReturn(Optional.empty());
//
//        ResponseEntity<Marca> resposta = _marcaController.deleta(1L);
//        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
//
//        verify(marcaRepository, never()).delete(any());
//    }
//
}