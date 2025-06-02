package com.rsdesenvolvimento.usuario.services;

import com.rsdesenvolvimento.usuario.modelo.dtos.UsuarioRequestDto;
import com.rsdesenvolvimento.usuario.modelo.dtos.UsuarioResponseDto;
import com.rsdesenvolvimento.usuario.modelo.entidades.Usuario;
import com.rsdesenvolvimento.usuario.modelo.mappers.UsuarioMapper;
import com.rsdesenvolvimento.usuario.repositorios.UsuarioRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioResponseDto cadastrar(UsuarioRequestDto dto) {
        if (this.usuarioRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com esse CPF");
        }
        if (this.usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com esse e-mail");
        }
        Usuario usuario = this.usuarioMapper.toEntity(dto);
        return this.usuarioMapper.toDto(this.usuarioRepository.save(usuario));
    }

    public UsuarioResponseDto buscarPorId(Long id) {
        Assert.notNull(id, "Id não pode ser nulo");
        Assert.isTrue(id > 0, "Id deve ser maior que zero");

        Usuario usuario = this.usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        return this.usuarioMapper.toDto(usuario);
    }

    public List<UsuarioResponseDto> listarTodos() {
        return this.usuarioRepository.findAll().stream().map(this.usuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    public void deletar(Long id) {
        Assert.notNull(id, "Id não pode ser nulo");
        Assert.isTrue(id > 0, "Id deve ser maior que zero");
        Usuario usuario = this.usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        this.usuarioRepository.delete(usuario);
    }

    public UsuarioResponseDto atualizar(Long id, UsuarioRequestDto dto) {
        Assert.notNull(id, "Id não pode ser nulo");
        Assert.notNull(id > 0, "Id deve ser maior que zero");

        Usuario usuario = this.usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        if (this.usuarioRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com esse CPF");
        }
        if (this.usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com esse e-mail");
        }
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setCpf(dto.getCpf());
        return this.usuarioMapper.toDto(this.usuarioRepository.save(usuario));
    }

}
