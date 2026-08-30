package br.com.fiap.techchallenge.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "Usuário", description = "DTO para representar um usuário")
public record UsuarioDto(
        @NotBlank(message = "Campo Obrigatório") String nome,
        @Email(message = "Campo Obrigatório") String email,
        @NotBlank(message = "Campo Obrigatório") String login,
        @NotBlank(message = "Campo Obrigatório") String senha,
        //@NotBlank LocalDateTime dataUltimaAlteracao,
        @Valid EnderecoDto endereco,
        @NotNull(message = "Campo Obrigatório") int tipoUsuario
) {
}
