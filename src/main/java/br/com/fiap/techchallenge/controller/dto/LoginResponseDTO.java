package br.com.fiap.techchallenge.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "LoginResponse", description = "DTO para representar o Token JWT de resposta da autenticação do usuário")
public record LoginResponseDTO(String token) {}