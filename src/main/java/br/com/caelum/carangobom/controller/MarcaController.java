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
    @Transactional
    public List<MarcaOutputDto> listar() {
        return _marcaService.listar();
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<MarcaOutputDto> procurarPeloId(@PathVariable Long id) {
        return _marcaService.procurarPeloId(id);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<MarcaOutputDto> cadastrar(@Valid @RequestBody MarcaInputDto marcaInput, UriComponentsBuilder uriBuilder) {
        MarcaOutputDto response = _marcaService.cadastrar(marcaInput);
        URI marcaURL = uriBuilder.path("/marcas/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(marcaURL).body(response);
    }

//    @PutMapping("/{id}")
//    @Transactional
//    public ResponseEntity<Marca> altera(@PathVariable Long id, @Valid @RequestBody Marca m1) {
//        Optional<Marca> m2 = mr.findById(id);
//        if (m2.isPresent()) {
//            Marca m3 = m2.get();
//            m3.setNome(m1.getNome());
//            return ResponseEntity.ok(m3);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    @Transactional
//    public ResponseEntity<Marca> deleta(@PathVariable Long id) {
//        Optional<Marca> m1 = mr.findById(id);
//        if (m1.isPresent()) {
//            Marca m2 = m1.get();
//            mr.delete(m2);
//            return ResponseEntity.ok(m2);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
   
}