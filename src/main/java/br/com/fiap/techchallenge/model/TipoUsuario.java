package br.com.fiap.techchallenge.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tipo_usuario")
public class TipoUsuario {

    @Id
    private Integer id;

    @Column(name = "descricao",  nullable = false)
    private String descricao;

}
