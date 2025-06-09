package com.rsdesenvolvimento.pedido_service.modelo.entidades;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Usuario {
    private String id;
    private String username;
    private String email;
}
