package br.com.caelum.carangobom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caelum.carangobom.model.entity.Usuario;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	Optional<Usuario> findByEmail(String email);

}
