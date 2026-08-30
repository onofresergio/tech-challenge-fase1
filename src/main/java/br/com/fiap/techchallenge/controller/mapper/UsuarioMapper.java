package br.com.fiap.techchallenge.controller.mapper;

import br.com.fiap.techchallenge.controller.dto.EnderecoDto;
import br.com.fiap.techchallenge.controller.dto.UsuarioDto;
import br.com.fiap.techchallenge.controller.dto.UsuarioResponseDto;
import br.com.fiap.techchallenge.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public UsuarioResponseDto toDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        // Isola a conversão do endereço também
        EnderecoDto enderecoDto = null;
        if (usuario.getEndereco() != null) {
            enderecoDto = new EnderecoDto(
                    usuario.getEndereco().getRua(),
                    usuario.getEndereco().getNumero(),
                    usuario.getEndereco().getCidade(),
                    usuario.getEndereco().getEstado(),
                    usuario.getEndereco().getCep()
            );
        }

        return new UsuarioResponseDto(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getLogin(),
                enderecoDto,
                usuario.getTipoUsuario().getId()
        );
    }
}
