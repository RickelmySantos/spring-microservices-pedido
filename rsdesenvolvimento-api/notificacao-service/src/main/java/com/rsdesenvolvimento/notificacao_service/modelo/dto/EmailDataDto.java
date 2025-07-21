package com.rsdesenvolvimento.notificacao_service.modelo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailDataDto {
    private String to;
    private String subject;
    private String content;
}
