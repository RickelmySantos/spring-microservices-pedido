package com.rsdesenvolvimento.usuario.controladores;

import com.rsdesenvolvimento.usuario.modelo.dtos.UsuarioRequestDto;
import com.rsdesenvolvimento.usuario.modelo.dtos.UsuarioResponseDto;
import com.rsdesenvolvimento.usuario.services.UsuarioService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
public class UsuarioApi {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> cadastrarUsuario(
            @RequestBody UsuarioRequestDto usuario) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.usuarioService.cadastrar(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.usuarioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> atualizarUsuario(@PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(this.usuarioService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable Long id) {
        this.usuarioService.deletar(id);
        ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listarAll() {
        return ResponseEntity.ok(this.usuarioService.listarTodos());
    }
}
