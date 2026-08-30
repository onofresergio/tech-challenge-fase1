package br.com.fiap.techchallenge.controller;

import br.com.fiap.techchallenge.controller.dto.LoginDto;
import br.com.fiap.techchallenge.controller.dto.SenhaDto;
import br.com.fiap.techchallenge.controller.dto.UsuarioDto;
import br.com.fiap.techchallenge.controller.dto.UsuarioResponseDto;
import br.com.fiap.techchallenge.controller.mapper.UsuarioMapper;
import br.com.fiap.techchallenge.model.Usuario;
import br.com.fiap.techchallenge.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Usuario", description = "API para gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioService usuarioService,  UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;
    }

    @GetMapping("usuarios")
    @Operation(summary = "Listar usuários", description = "Retorna uma lista de todos os) usuários cadastrados no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
            @ApiResponse(responseCode = "204", description = "Nenhum usuário encontrado")
    })
    public ResponseEntity<List<UsuarioResponseDto>> listarUsuarios() {
        List<Usuario> usuarios = usuarioService.findAllUsuarios();
        if (usuarios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<UsuarioResponseDto> dto = usuarios.stream()
                .map(usuarioMapper::toDto)
                .toList();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("usuarios/{nome}")
    @Operation(summary = "Buscar usuário por nome", description = "Busca usuários pelo nome fornecido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuários encontrados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhum usuário encontrado com o nome fornecido")
    })
    public ResponseEntity<List<UsuarioResponseDto>> buscarUsuarioPorNome(@PathVariable @Valid String nome) {
        List<Usuario> usuarios = usuarioService.buscarPorNomeContainingIgnoreCase(nome);
        if (usuarios != null &&  !usuarios.isEmpty()) {
            List<UsuarioResponseDto> dto = usuarios.stream()
                    .map(usuarioMapper::toDto)
                    .toList();
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/usuarios")
    @Operation(summary = "Salvar usuário", description = "Salva um novo usuário no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "422", description = "Erro de validação dos dados enviados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> salvar(@RequestBody @Valid UsuarioDto usuario) {
        usuarioService.salvar(usuario);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/usuarios/{id}")
    @Operation(summary = "Deletar usuário", description = "Deleta um usuário existente pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> deletarUsuario(@PathVariable int id, @RequestBody @Valid LoginDto login) {

        Optional<Usuario> usuarioOptions = usuarioService.buscarPorId(id);

        if (!usuarioService.autenticar(login.login(), login.senha())) {
            return ResponseEntity.badRequest().build();
        }else if (usuarioOptions.isPresent()) {
            usuarioService.deleteById(usuarioOptions.get().getId());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/usuarios/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza um usuário existente no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "422", description = "Erro de validação dos dados enviados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> atualizarUsuario(@PathVariable int id, @RequestBody @Valid UsuarioDto dto) {

        Optional<Usuario> usuarioOptional = usuarioService.buscarPorId(id);

        if (!usuarioOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }else if (!usuarioService.autenticar(usuarioOptional.get().getLogin(), dto.senha())) {
            return ResponseEntity.badRequest().build();
        } else {
            var usuario = usuarioOptional.get();
            var endereco = usuario.getEndereco();

            usuario.setNome(dto.nome());
            usuario.setEmail(dto.email());
            usuario.setLogin(dto.login());

            /* #########################################
            // Alteração de senha somente pelo
            // endpoint /usuarios/{id}/senha
            // usuario.setSenha(dto.senha());
            // ######################################### */

            endereco.setCep(dto.endereco().cep());
            endereco.setRua(dto.endereco().rua());
            endereco.setNumero(dto.endereco().numero());
            endereco.setCidade(dto.endereco().cidade());
            endereco.setEstado(dto.endereco().estado());

            usuario.setEndereco(endereco);

            usuarioService.atualizar(usuario);
            return ResponseEntity.noContent().build();

        }
    }

    @PutMapping("/usuarios/{id}/senha")
    @Operation(summary = "Atualizar senha do usuário", description = "Atualiza a senha de um usuário existente no sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Senha do usuário atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "422", description = "Erro de validação dos dados enviados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> atualizarSenha(@PathVariable @Valid int id, @RequestBody @Valid SenhaDto senhaDto) {

        Optional<Usuario> usuarioOptional = usuarioService.buscarPorId(id);
        if (!usuarioOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        var usuario = usuarioOptional.get();

        if (!senhaDto.novaSenha().equals(senhaDto.confirmaSenha())) {
            return ResponseEntity.badRequest().build();
        }

        usuarioService.atualizarSenha(usuario, senhaDto.novaSenha());
        return ResponseEntity.noContent().build();
    }

}
