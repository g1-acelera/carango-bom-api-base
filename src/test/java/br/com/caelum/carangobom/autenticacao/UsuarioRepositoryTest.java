package br.com.caelum.carangobom.autenticacao;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import br.com.caelum.carangobom.model.entity.Usuario;
import br.com.caelum.carangobom.repository.UsuarioRepository;


@DataJpaTest
@TestPropertySource(properties = {"DB_NAME=carangobom-test", "spring.jpa.hibernate.ddlAuto:create-drop"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioRepositoryTest {
	
	private Usuario usuarioLogin;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TestEntityManager entityManager;
    
    @BeforeEach
    void setup() {
    	usuarioLogin = new Usuario("teste@teste", "senha");
    	entityManager.persist(usuarioLogin);
    }

	@Test
	void deveriaCarregarUmUsuarioAoBuscarPeloEmail() {
		Usuario usuario = usuarioLogin = new Usuario("teste@teste", "senha");
		Optional<Usuario> usuarioBanco = repository.findByEmail(usuario.getEmail());
		assertThat(usuarioBanco).isPresent();
	}
	
	@Test
	void naoDeveriaCarregarUmUsuarioCujoEmailNaoEstejaCadastrado() {
		Usuario usuario = usuarioLogin = new Usuario("teste2@teste", "senha");
		Optional<Usuario> usuarioBanco = repository.findByEmail(usuario.getEmail());
		assertThat(usuarioBanco).isEmpty();
	}

}