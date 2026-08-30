package br.com.fiap.techchallenge.service;

import br.com.fiap.techchallenge.controller.dto.UsuarioDto;
import br.com.fiap.techchallenge.exception.RegraNegocioException;
import br.com.fiap.techchallenge.model.Endereco;
import br.com.fiap.techchallenge.model.TipoUsuario;
import br.com.fiap.techchallenge.model.Usuario;
import br.com.fiap.techchallenge.repository.TipoUsuarioRepository;
import br.com.fiap.techchallenge.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          TipoUsuarioRepository tipoUsuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tipoUsuarioRepository = tipoUsuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void salvar(UsuarioDto usuarioDto){

        TipoUsuario tipo = tipoUsuarioRepository.findById(usuarioDto.tipoUsuario())
                .orElseThrow(() -> new RegraNegocioException("Tipo de usuário não encontrado para o ID: " + usuarioDto.tipoUsuario()));

        if (usuarioRepository.existsByEmail(usuarioDto.email())) {
            throw new RegraNegocioException("E-mail já cadastrado no sistema.");
        }

        String senhaCriptografada = passwordEncoder.encode(usuarioDto.senha());

        Endereco endereco = new Endereco(
                usuarioDto.endereco().rua(),
                usuarioDto.endereco().numero(),
                usuarioDto.endereco().cidade(),
                usuarioDto.endereco().estado(),
                usuarioDto.endereco().cep()
        );


        Usuario usuario = new Usuario(
                usuarioDto.nome(),
                usuarioDto.email(),
                usuarioDto.login(),
                senhaCriptografada,
                endereco,
                tipo
        );

        usuarioRepository.save(usuario);
    }

    public List<Usuario> buscarPorNomeContainingIgnoreCase(String nome) {

        if (nome == null || nome.trim().isEmpty()) {
            return List.of();
        }
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Optional<Usuario> buscarPorId(int id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> findAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public void deleteById(int id) {
        usuarioRepository.deleteById(id);
    }


    public void atualizar(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public void atualizarSenha(Usuario usuario, String senha) {
        String senhaCriptografada = passwordEncoder.encode(senha);
        usuario.setSenha(senhaCriptografada);
        usuarioRepository.save(usuario);
    }

    public Usuario findByLogin(String login) {
        Usuario usuario = usuarioRepository.findByLogin(login);
        if (usuario != null) {
            return usuario;
        } else {
            throw new RegraNegocioException("Usuário não encontrado para o login: " + login);
        }
    }

    public boolean autenticar(String login, String senha) {
        Usuario usuario = usuarioRepository.findByLogin(login);
        if (usuario != null) {
            return passwordEncoder.matches(senha, usuario.getSenha());
        }
        else {
            return false;
        }
    }
}
