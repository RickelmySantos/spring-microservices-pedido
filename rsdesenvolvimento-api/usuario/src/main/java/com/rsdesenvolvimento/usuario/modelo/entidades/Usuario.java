package com.rsdesenvolvimento.usuario.modelo.entidades;

import com.rsdesenvolvimento.usuario.core.modelo.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.Audited;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Audited
@AuditOverride(forClass = EntidadeBase.class)
@SequenceGenerator(name = EntidadeBase.SEQUENCE_GENERATOR, sequenceName = "SQ_USUARIO",
        initialValue = 1, allocationSize = 1)
@Table(name = "TB_USUARIO", uniqueConstraints = {})
public class Usuario extends EntidadeBase {


    private static final long serialVersionUID = 1L;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;
    @Column(name = "CPF", nullable = false, length = 11)
    private String cpf;
    @Column(name = "EMAIL", nullable = false, length = 50)
    private String email;
    @Column(name = "ATIVO", nullable = false)
    private boolean ativo;

}
