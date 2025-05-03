package com.rsdesenvolvimento.usuario.modelo.entidades;

import com.rsdesenvolvimento.usuario.core.modelo.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Usuario extends EntidadeBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, length = 100)
  private String nome;
  @Column(nullable = false, length = 11)
  private String cpf;
  @Column(nullable = false, length = 50)
  private String email;
  @Column(nullable = false, length = 20)
  private boolean ativo;

}
