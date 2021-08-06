package br.com.caelum.carangobom.controller;

import br.com.caelum.carangobom.model.dto.VeiculoInputDto;
import br.com.caelum.carangobom.model.dto.VeiculoOutputDto;
import br.com.caelum.carangobom.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    private VeiculoService veiculoService;

    @Autowired
    public VeiculoController(VeiculoService veiculoService) {
       this.veiculoService = veiculoService;
    }

    @GetMapping
    @Cacheable(value = "listaVeiculos")
    public List<VeiculoOutputDto> listar() {
        return veiculoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoOutputDto> procurarPeloId(@PathVariable Long id) {
        return veiculoService.procurarPeloId(id);
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "listaVeiculos", allEntries = true)
    public ResponseEntity<VeiculoOutputDto> cadastrar(@Valid @RequestBody VeiculoInputDto veiculoInput, UriComponentsBuilder uriBuilder) {
        VeiculoOutputDto response = veiculoService.cadastrar(veiculoInput);
        URI veiculoURL = uriBuilder.path("/veiculos/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(veiculoURL).body(response);
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaVeiculos", allEntries = true)
    public ResponseEntity<VeiculoOutputDto> alterar(@PathVariable Long id, @Valid @RequestBody VeiculoInputDto veiculoInput) {
        return veiculoService.alterar(id, veiculoInput);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaVeiculos", allEntries = true)
    public ResponseEntity<VeiculoOutputDto> deletar(@PathVariable Long id) {
        return veiculoService.deletar(id);
    }
   
}