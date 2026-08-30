package br.com.fiap.techchallenge.repository;

import br.com.fiap.techchallenge.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
}
