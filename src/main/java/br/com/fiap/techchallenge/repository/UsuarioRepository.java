package br.com.fiap.techchallenge.repository;

import br.com.fiap.techchallenge.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByEmail(String email);

    Usuario findByLogin(String login);

    List<Usuario> findByNome(String nome);
    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    void deleteById(int id);
}
