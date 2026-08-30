package br.com.fiap.techchallenge.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Authentication", description = "DTO para representar a autenticação do usuário")
public record AuthenticationDTO(
        @NotBlank(message = "O campo login é obrigatório")
        String login,

        @NotBlank(message = "O campo senha é obrigatório")
        String senha
) {}