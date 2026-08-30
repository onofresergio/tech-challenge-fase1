package br.com.fiap.techchallenge.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;


@Schema(name = "Usuário Response", description = "DTO para representar o retorno de um usuário sem informações sensíveis")
public record UsuarioResponseDto(
        String nome,
        String email,
        String login,
        EnderecoDto endereco,
        int tipoUsuario
) {
}
