package com.rsdesenvolvimento.estoque.modelo.entidade;

import com.rsdesenvolvimento.estoque.core.modelo.entidade.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
@Table(name = "TB_ESTOQUE", uniqueConstraints = {})
@SequenceGenerator(name = EntidadeBase.SEQUENCE_GENERATOR, sequenceName = "SQ_ESTOQUE",
        initialValue = 1, allocationSize = 1)
public class Estoque extends EntidadeBase {

    private static final long serialVersionUID = 1L;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;
    @Column(name = "DESCRICAO", nullable = false, length = 255)
    private String descricao;
    @Column(name = "PRECO", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
    @Column(name = "CATEGORIA", nullable = false, length = 50)
    private String categoria;
    @Column(name = "ESTOQUE", nullable = false)
    private Integer estoque;

    private String imagemUrl;
}
