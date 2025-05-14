package com.rsdesenvolvimento.usuario.modelo.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDto {

  @NotBlank(message = "Nome é obrigatório")
  @Size(max = 100, message = "Nome deve ter até 100 caracteres")
  private String nome;

  @NotBlank(message = "CPF é obrigatório")
  @CPF(message = "CPF inválido")
  private String cpf;

  @NotBlank(message = "Email é obrigatório")
  @Email(message = "Email inválido")
  @Size(max = 50)
  private String email;

  private boolean ativo;
}
