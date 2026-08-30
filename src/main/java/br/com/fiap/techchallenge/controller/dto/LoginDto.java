package br.com.fiap.techchallenge.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Login", description = "DTO para representar o login do usuário")
public record LoginDto(
        @NotBlank(message = "O campo login é obrigatório")
        String login,

        @NotBlank(message = "O campo senha é obrigatório")
        String senha) {
}
