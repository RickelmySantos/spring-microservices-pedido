package com.rsdesenvolvimento.pedido_service.modelo.entidades;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Usuario {


    @JsonProperty("sub")
    private String id;

    @JsonProperty("preferred_username")
    private String username;

    @JsonProperty("email")
    private String email;
}
