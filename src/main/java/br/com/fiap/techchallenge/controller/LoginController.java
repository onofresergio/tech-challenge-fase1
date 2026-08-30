package br.com.fiap.techchallenge.controller;

import br.com.fiap.techchallenge.controller.dto.LoginDto;
import br.com.fiap.techchallenge.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/autenticacao")
@Tag(name = "Autenticação", description = "API para autenticação de usuários")
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login de usuário", description = "Autentica um usuário com base no login e senha fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos")
    })
    public ResponseEntity<String> login(@RequestBody LoginDto dto) {
        boolean loginSucesso = usuarioService.autenticar(dto.login(), dto.senha());

        if (loginSucesso) {
            return ResponseEntity.ok("Login realizado com sucesso!");
        } else {
            // Retorne 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos.");
        }
    }

}
