
package br.com.caelum.carangobom.controller;

import br.com.caelum.carangobom.model.dto.UsuarioInputDto;
import br.com.caelum.carangobom.model.dto.UsuarioOutputDto;
import br.com.caelum.carangobom.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
       this.usuarioService = usuarioService;
    }

    @GetMapping
    @Cacheable(value = "listaUsuario")
    public List<UsuarioOutputDto> lista() {
        return usuarioService.listar();
    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "listaUsuario", allEntries = true)
    public ResponseEntity<UsuarioOutputDto> cadastra(@Valid @RequestBody UsuarioInputDto usuarioInput, UriComponentsBuilder uriBuilder) {
    	return usuarioService.cadastrar(usuarioInput);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaUsuario", allEntries = true)
    public ResponseEntity<UsuarioOutputDto> deleta(@PathVariable Long id) {
        return usuarioService.deletar(id);
    }
   
}