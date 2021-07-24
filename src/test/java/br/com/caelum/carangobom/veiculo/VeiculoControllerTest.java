package br.com.caelum.carangobom.veiculo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.caelum.carangobom.controller.VeiculoController;
import br.com.caelum.carangobom.model.dto.VeiculoInputDto;
import br.com.caelum.carangobom.model.dto.VeiculoOutputDto;
import br.com.caelum.carangobom.model.entity.Marca;
import br.com.caelum.carangobom.model.entity.Veiculo;
import br.com.caelum.carangobom.service.VeiculoService;

class VeiculoControllerTest {
	
	private static final Marca VW = new Marca(1L, "Volkswagen");
	private static final Marca AUDI = new Marca(2L, "Audi");
	private static final Marca FIAT = new Marca(3L, "Fiat");
	
	private UriComponentsBuilder uriBuilder;
	private VeiculoController controller;
    private VeiculoInputDto inputDto;
    private List<Veiculo> veiculos;
	
	@Mock
	private VeiculoService service;
	
	
	@BeforeEach
	public void setup() {
		openMocks(this);
		controller = new VeiculoController(service);
		uriBuilder = UriComponentsBuilder.fromUriString("http://localhost:8080");
		
		inputDto = new VeiculoInputDto(
				"Gol Quadrado das Quebradas", 1L, 1990, 10000
		);

		veiculos = List.of(
				new Veiculo(1L, "Voyage CL 1.6 AP", 5000, 1994, VW), 
				new Veiculo(2L, "S4", 549000.500, 2021, AUDI), 
				new Veiculo(3L, "Uno", 8900, 2001, FIAT)
		);
	}
	
	@Test
    void deveRetornarListaQuandoHouverDados() {
		List<VeiculoOutputDto> listaEsperada = VeiculoOutputDto.convertToDto(veiculos);
		
		when(service.listar()).thenReturn(listaEsperada);
		
		assertEquals(listaEsperada, controller.listar());
    }

    @Test
    void deveRetornarPeloId() {
    	var outputDto = new VeiculoOutputDto(veiculos.get(0));
    	var veiculoResposta = ResponseEntity.ok(outputDto);
    	
    	when(service.procurarPeloId(1L)).thenReturn(veiculoResposta);
    	
    	var resposta = controller.procurarPeloId(1L);
    	
    	assertEquals(outputDto, resposta.getBody());
    	assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void deveRetornarNotFoundQuandoProcurarComIdInexistente() {
    	when(service.procurarPeloId(anyLong()))
        	.thenReturn(ResponseEntity.notFound().build());
    	
    	var resposta = controller.procurarPeloId(10L);
    	
    	assertNull(resposta.getBody());
    	assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveResponderComCreatedEUrlQuandoCadastrar() {
    	final String URL_ESPERADA = "http://localhost:8080/veiculos/1";
    	
    	when(service.cadastrar(inputDto))
    		.thenReturn(
    				new VeiculoOutputDto(
    						new Veiculo(
    								1L, 
    								inputDto.getNome(),
    								inputDto.getValor(),
    								inputDto.getAno(),
    								VW
    						)
    				)
    		);
    	
    	var resposta = controller.cadastrar(inputDto, uriBuilder);
    	
    	assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
    	assertEquals(URL_ESPERADA, resposta.getHeaders().getLocation().toString());			
    }

    @Test
    void deveAlterarExistente() {
    	Veiculo veiculo = new Veiculo(1L, 
    								"Parati GLS",
									inputDto.getValor(),
									inputDto.getAno(),
									VW);
         
        ResponseEntity<VeiculoOutputDto> veiculoResposta = 
        		ResponseEntity.ok(new VeiculoOutputDto(veiculo));
         
        when(service.alterar(1L, inputDto)).thenReturn(veiculoResposta);

        ResponseEntity<VeiculoOutputDto> resposta = controller.alterar(1L, inputDto);
        
        assertEquals(HttpStatus.OK, resposta.getStatusCode());

        VeiculoOutputDto veiculoAlterado = resposta.getBody();
        assertEquals("Parati GLS", veiculoAlterado.getNome());
    }

    @Test
    void naoDeveAlterarInexistente() {    	
    	when(service.alterar(2L, inputDto))
        	.thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<VeiculoOutputDto> resposta = controller.alterar(2L, inputDto);
        
        assertNull(resposta.getBody());
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

    @Test
    void deveDeletarExistente() {	
    	Veiculo veiculo = new Veiculo(1L, 
									"Parati GLS",
									inputDto.getValor(),
									inputDto.getAno(),
									VW);

        
    	ResponseEntity<VeiculoOutputDto> veiculoResposta = 
        		ResponseEntity.ok(new VeiculoOutputDto(veiculo));
        
        when(service.deletar(1L))
        	.thenReturn(veiculoResposta);

        ResponseEntity<VeiculoOutputDto> resposta = controller.deletar(1L);
        
        assertEquals(veiculoResposta.getBody(), resposta.getBody());
        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }

    @Test
    void naoDeveDeletarInexistente() {
    	when(service.deletar(2L))
        	.thenReturn(ResponseEntity.notFound().build());
        
        ResponseEntity<VeiculoOutputDto> resposta = controller.deletar(2L);
        
        assertNull(resposta.getBody());
        assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
    }

}
