package com.rsdesenvolvimento.usuario.services;

import com.rsdesenvolvimento.usuario.modelo.dtos.UsuarioRequestDto;
import com.rsdesenvolvimento.usuario.modelo.dtos.UsuarioResponseDto;
import com.rsdesenvolvimento.usuario.modelo.entidades.Usuario;
import com.rsdesenvolvimento.usuario.modelo.mappers.UsuarioMapper;
import com.rsdesenvolvimento.usuario.repositorios.UsuarioRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    // CADASTRAR

    @Test
    void dada_umaEntidadeValida_quandoCadastrar_EntaoDeveRetornar_EntidadeCadastrado() {
        UsuarioRequestDto dto =
                new UsuarioRequestDto("xpto", "12345678900", "xpto@gmail.com", true);
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setCpf(dto.getCpf());
        usuario.setEmail(dto.getEmail());
        usuario.setAtivo(dto.isAtivo());

        UsuarioResponseDto responseDto = new UsuarioResponseDto();
        responseDto.setNome(dto.getNome());
        responseDto.setCpf(dto.getCpf());
        responseDto.setEmail(dto.getEmail());
        responseDto.setAtivo(dto.isAtivo());

        Mockito.when(this.usuarioRepository.existsByCpf(dto.getCpf())).thenReturn(false);
        Mockito.when(this.usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        Mockito.when(this.usuarioMapper.toEntity(dto)).thenReturn(usuario);
        Mockito.when(this.usuarioRepository.save(usuario)).thenReturn(usuario);
        Mockito.when(this.usuarioMapper.toDto(usuario)).thenReturn(responseDto);

        UsuarioResponseDto result = this.usuarioService.cadastrar(dto);

        Assertions.assertNotNull(result, "Resultado não deve ser nulo");
        Assertions.assertEquals(dto.getNome(), result.getNome(), "Nome deve ser igual");
        Assertions.assertEquals(dto.getCpf(), result.getCpf(), "CPF deve ser igual");
        Assertions.assertEquals(dto.getEmail(), result.getEmail(), "Email deve ser igual");
        Assertions.assertEquals(dto.isAtivo(), result.isAtivo(), "Ativo deve ser igual");

        Mockito.verify(this.usuarioRepository).existsByCpf(dto.getCpf());
        Mockito.verify(this.usuarioRepository).existsByEmail(dto.getEmail());
        Mockito.verify(this.usuarioMapper).toEntity(dto);
        Mockito.verify(this.usuarioRepository).save(usuario);
        Mockito.verify(this.usuarioMapper).toDto(usuario);

    }

    // BUSCAR POR ID
    @Test
    void dado_IdInvalido_quandoBuscarPorId_EntaoDeveLancarExcecao() {

        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    this.usuarioService.buscarPorId(null);
                });
        Assertions.assertEquals(exception.getMessage(), "Id não pode ser nulo");
    }

    @Test
    void dado_IdMenorQueZero_quandoBuscarPorId_EntaoDeveLancarExcecao() {
        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    this.usuarioService.buscarPorId(-1L);
                });
        Assertions.assertEquals(exception.getMessage(), "Id deve ser maior que zero");
    }

    @Test
    void dado_MetodoBuscarPorId_quandoUsuarioNaoEncontrado_EntaoDeveLancarExcecao() {
        Long id = 1L;
        Mockito.when(this.usuarioRepository.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    this.usuarioService.buscarPorId(id);
                });

        Assertions.assertEquals("Usuário não encontrado", exception.getMessage());

        Mockito.verify(this.usuarioRepository).findById(id);
    }

    @Test
    void dado_EntidadeValida_quandoBuscarPorId_EntaoDeveRetornar_Entidade() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("xpto");
        usuario.setCpf("12345678900");
        usuario.setEmail("xpto@gmail,com");
        usuario.setAtivo(true);

        UsuarioResponseDto responseDto = new UsuarioResponseDto();
        responseDto.setId(id);
        responseDto.setNome(usuario.getNome());
        responseDto.setCpf(usuario.getCpf());
        responseDto.setEmail(usuario.getEmail());
        responseDto.setAtivo(usuario.isAtivo());

        Mockito.when(this.usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        Mockito.when(this.usuarioMapper.toDto(usuario)).thenReturn(responseDto);
        UsuarioResponseDto result = this.usuarioService.buscarPorId(id);

        Assertions.assertNotNull(result, "Resultado não deve ser nulo");
        Assertions.assertEquals(id, result.getId(), "Id deve ser igual");
        Assertions.assertEquals(usuario.getNome(), result.getNome(), "Nome deve ser igual");
        Assertions.assertEquals(usuario.getCpf(), result.getCpf(), "CPF deve ser igual");
        Assertions.assertEquals(usuario.getEmail(), result.getEmail(), "Email deve ser igual");
        Assertions.assertEquals(usuario.isAtivo(), result.isAtivo(), "Ativo deve ser igual");
        Mockito.verify(this.usuarioRepository).findById(id);
        Mockito.verify(this.usuarioMapper).toDto(usuario);
    }

    // LISTAR TODOS
    @Test
    void dado_MetodoListarTodos_quandoNaoExistirUsuariosCadastrados_EntaoDeveRetornarListaVazia() {
        Mockito.when(this.usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        var result = this.usuarioService.listarTodos();

        Assertions.assertNotNull(result, "Resultado não deve ser nulo");
        Assertions.assertTrue(result.isEmpty(), "Lista deve estar vazia");
        Mockito.verify(this.usuarioRepository).findAll();

    }

    @Test
    void dado_MetodoListarTodos_quandoExistirUsuariosCadastrados_EntaoDeveRetornarListaComUsuarios() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Usuario 1");
        usuario1.setCpf("12345678901");
        usuario1.setEmail("usuario1@gmail.com");
        usuario1.setAtivo(true);

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNome("Usuario 2");
        usuario2.setCpf("12345678902");
        usuario2.setEmail("usuario2@gmail.com");
        usuario2.setAtivo(true);

        UsuarioResponseDto responseDto1 = new UsuarioResponseDto();
        responseDto1.setId(usuario1.getId());
        responseDto1.setNome(usuario1.getNome());
        responseDto1.setCpf(usuario1.getCpf());
        responseDto1.setEmail(usuario1.getEmail());
        responseDto1.setAtivo(usuario1.isAtivo());

        UsuarioResponseDto responseDto2 = new UsuarioResponseDto();
        responseDto2.setId(usuario2.getId());
        responseDto2.setNome(usuario2.getNome());
        responseDto2.setCpf(usuario2.getCpf());
        responseDto2.setEmail(usuario2.getEmail());
        responseDto2.setAtivo(usuario2.isAtivo());

        Mockito.when(this.usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2));
        Mockito.when(this.usuarioMapper.toDto(usuario1)).thenReturn(responseDto1);
        Mockito.when(this.usuarioMapper.toDto(usuario2)).thenReturn(responseDto2);

        List<UsuarioResponseDto> result = this.usuarioService.listarTodos();

        Assertions.assertNotNull(result, "Resultado não deve ser nulo");
        Assertions.assertEquals(2, result.size(), "Lista deve conter dois usuários");
        Assertions.assertEquals(usuario1.getId(), result.get(0).getId(),
                "Id do primeiro usuário deve ser igual");
        Assertions.assertEquals(usuario1.getNome(), result.get(0).getNome(),
                "Nome do primeiro usuário deve ser igual");
        Assertions.assertEquals(usuario1.getCpf(), result.get(0).getCpf(),
                "CPF do primeiro usuário deve ser igual");
        Assertions.assertEquals(usuario1.getEmail(), result.get(0).getEmail(),
                "Email do primeiro usuário deve ser igual");

        Assertions.assertEquals(usuario2.getId(), result.get(1).getId(),
                "Id do segundo usuário deve ser igual");
        Assertions.assertEquals(usuario2.getNome(), result.get(1).getNome(),
                "Nome do segundo usuário deve ser igual");
        Mockito.verify(this.usuarioRepository).findAll();
        Mockito.verify(this.usuarioMapper).toDto(usuario1);
        Mockito.verify(this.usuarioMapper).toDto(usuario2);
    }


    // ATUALIZAR

    @Test
    void dado_IdInvalido_quandoAtualizar_EntaoDeveLancarExcecao() {
        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    this.usuarioService.atualizar(null, new UsuarioRequestDto());
                });

        Assertions.assertEquals("Id não pode ser nulo", exception.getMessage());
        Mockito.verifyNoInteractions(this.usuarioRepository);
    }

    @Test
    void dado_IdValido_quandoChamarAtualizar_EntaoDeveRetornarUsuarioAtualizado() {
        Long id = 1L;
        UsuarioRequestDto dto = new UsuarioRequestDto("xptoATUALIZADO", "12345678900",
                "xptoAtualizado@gmail.com", true);

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("xpto");
        usuario.setCpf("12345678900");
        usuario.setEmail("xpto@gmail,com");
        usuario.setAtivo(true);

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setNome(dto.getNome());
        usuarioAtualizado.setCpf(dto.getCpf());
        usuarioAtualizado.setEmail(dto.getEmail());
        usuarioAtualizado.setAtivo(dto.isAtivo());

        UsuarioResponseDto responseDto = new UsuarioResponseDto();
        responseDto.setId(id);
        responseDto.setNome(usuarioAtualizado.getNome());
        responseDto.setCpf(usuarioAtualizado.getCpf());
        responseDto.setEmail(usuarioAtualizado.getEmail());
        responseDto.setAtivo(usuarioAtualizado.isAtivo());

        Mockito.when(this.usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        Mockito.when(this.usuarioRepository.existsByCpf(dto.getCpf())).thenReturn(false);
        Mockito.when(this.usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        Mockito.when(this.usuarioRepository.save(usuario)).thenReturn(usuarioAtualizado);
        Mockito.when(this.usuarioMapper.toDto(usuarioAtualizado)).thenReturn(responseDto);

        UsuarioResponseDto result = this.usuarioService.atualizar(id, dto);

        Assertions.assertNotNull(result, "Resultado não deve ser nulo");
        Assertions.assertEquals(id, result.getId(), "Id deve ser igual");
        Assertions.assertEquals(dto.getNome(), result.getNome(), "Nome deve ser igual");
        Assertions.assertEquals(dto.getCpf(), result.getCpf(), "CPF deve ser igual");
        Assertions.assertEquals(dto.getEmail(), result.getEmail(), "Email deve ser igual");
        Assertions.assertEquals(dto.isAtivo(), result.isAtivo(), "Ativo deve ser igual");

        Mockito.verify(this.usuarioRepository).findById(id);
        Mockito.verify(this.usuarioRepository).existsByCpf(dto.getCpf());
        Mockito.verify(this.usuarioRepository).existsByEmail(dto.getEmail());
        Mockito.verify(this.usuarioRepository).save(usuario);
        Mockito.verify(this.usuarioMapper).toDto(usuarioAtualizado);

    }

    // DELETAR

    @Test
    void dado_IdInvalido_quandoDeletar_EntaoDeveLancarExcecao() {
        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    this.usuarioService.deletar(null);
                });

        Assertions.assertEquals("Id não pode ser nulo", exception.getMessage());
        Mockito.verifyNoInteractions(this.usuarioRepository);

    }

    @Test
    void dado_IdMaiorQueZero_quandoDeletar_EntaoDeveLancarExcecao() {
        IllegalArgumentException exception =
                Assertions.assertThrows(IllegalArgumentException.class, () -> {
                    this.usuarioService.deletar(-1L);
                });

        Assertions.assertEquals("Id deve ser maior que zero", exception.getMessage());
        Mockito.verifyNoInteractions(this.usuarioRepository);
    }

    @Test
    void dado_IdValido_quandoChamarDeletar_EntaoDeveDeletarUsuario() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("xpto");
        usuario.setCpf("12345678900");
        usuario.setEmail("xpto@gmail,com");
        usuario.setAtivo(true);

        Mockito.when(this.usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        this.usuarioService.deletar(id);

        Mockito.verify(this.usuarioRepository).findById(id);
        Mockito.verify(this.usuarioRepository).delete(usuario);
    }
}
