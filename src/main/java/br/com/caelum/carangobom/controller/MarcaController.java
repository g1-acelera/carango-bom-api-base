package br.com.caelum.carangobom.controller;

import br.com.caelum.carangobom.model.dto.MarcaInputDto;
import br.com.caelum.carangobom.model.dto.MarcaOutputDto;
import br.com.caelum.carangobom.model.entity.Marca;
import br.com.caelum.carangobom.repository.MarcaRepository;
import br.com.caelum.carangobom.service.MarcaService;
import br.com.caelum.carangobom.util.validacao.ErroDeParametroOutputDto;
import br.com.caelum.carangobom.util.validacao.ListaDeErrosOutputDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/marcas")
public class MarcaController {

    private MarcaService _marcaService;

    @Autowired
    public MarcaController(MarcaService marcaService) {
       this._marcaService = marcaService;
    }

    @GetMapping
    public List<MarcaOutputDto> lista() {
        return _marcaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MarcaOutputDto> procuraPeloId(@PathVariable Long id) {
        return _marcaService.procurarPeloId(id);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<MarcaOutputDto> cadastra(@Valid @RequestBody MarcaInputDto marcaInput, UriComponentsBuilder uriBuilder) {
        MarcaOutputDto response = _marcaService.cadastrar(marcaInput);
        URI marcaURL = uriBuilder.path("/marcas/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(marcaURL).body(response);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<MarcaOutputDto> altera(@PathVariable Long id, @Valid @RequestBody MarcaInputDto marcaInput) {
        return _marcaService.alterar(id, marcaInput);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<MarcaOutputDto> deleta(@PathVariable Long id) {
        return _marcaService.deletar(id);
    }
   
}