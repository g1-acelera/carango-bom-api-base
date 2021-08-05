package br.com.caelum.carangobom.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.caelum.carangobom.model.dto.UsuarioInputDto;
import br.com.caelum.carangobom.model.dto.UsuarioOutputDto;
import br.com.caelum.carangobom.model.entity.Usuario;
import br.com.caelum.carangobom.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioService implements UserDetailsService {	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private static final String MSGERROUSUARIO = "Dados inválidos!";

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<Usuario> usuario = usuarioRepository.findByEmail(username);
		if (usuario.isPresent()) {
			return usuario.get();
		}
		
		throw new UsernameNotFoundException(MSGERROUSUARIO);
	}
	
    public List<UsuarioOutputDto> listar() {
        List<Usuario> usuario = usuarioRepository.findAll();
        try {
			return UsuarioOutputDto.convertToDto(usuario);
		}
		catch (NullPointerException e) {
			return Collections.emptyList();
		}
    }

    public ResponseEntity<UsuarioOutputDto> cadastrar(UsuarioInputDto usuarioInput) {
    	Optional<Usuario> usuario = usuarioRepository.findByEmail(usuarioInput.getEmail());
		if (!usuario.isPresent()) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        	Usuario novoUsuario = usuarioRepository.save(new Usuario(usuarioInput.getEmail(), encoder.encode(usuarioInput.getSenha())));
        	return ResponseEntity.ok(new UsuarioOutputDto(novoUsuario));
		}
		
		return ResponseEntity.badRequest().build();
    }


    public ResponseEntity<UsuarioOutputDto> deletar(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);

        if (usuario.isPresent()) {
        	Usuario usuarioDeletar = usuario.get();
            usuarioRepository.delete(usuarioDeletar);

            return ResponseEntity.ok(new UsuarioOutputDto(usuarioDeletar));
        }

        return ResponseEntity.notFound().build();
    }

}
