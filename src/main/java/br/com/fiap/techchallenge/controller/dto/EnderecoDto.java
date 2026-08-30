package br.com.fiap.techchallenge.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Endereço", description = "DTO para representar o endereço do usuário")
public record EnderecoDto(
        @NotBlank String rua,
        @NotBlank String numero,
        @NotBlank String cidade,
        @NotBlank String estado,
        @NotBlank String cep
) {
}
