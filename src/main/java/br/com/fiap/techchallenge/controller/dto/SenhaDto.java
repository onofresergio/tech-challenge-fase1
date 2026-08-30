package br.com.fiap.techchallenge.controller.dto;

import br.com.fiap.techchallenge.controller.validation.FieldsValueMatch;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@FieldsValueMatch(
        field = "novaSenha",
        fieldMatch = "confirmaSenha",
        message = "A nova senha e a confirmação de senha devem ser iguais!"
)
@Schema(name = "Senha", description = "DTO para representar alteração de senha do usuário")
public record SenhaDto(
        @NotBlank(message = "O campo nova senha é obrigatório")
        String novaSenha,
        @NotBlank(message = "O campo confirma senha é obrigatório")
        String confirmaSenha) {
}
