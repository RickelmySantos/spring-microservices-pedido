package com.rsdesenvolvimento.pedido_service.modelo.entidades;

import com.rsdesenvolvimento.pedido_service.core.modelo.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Audited
@AuditOverride(forClass = EntidadeBase.class)
@Table(name = "TB_ITEM_PEDIDO", uniqueConstraints = {})
@SequenceGenerator(name = EntidadeBase.SEQUENCE_GENERATOR, sequenceName = "SQ_ITEM_PEDIDO",
        initialValue = 1, allocationSize = 1)
public class ItemPedido extends EntidadeBase {
    private static final long serialVersionUID = 1L;

    @Column(name = "PRODUTO_ID", nullable = false)
    private Long produtoId;

    @Column(name = "NOME_PRODUTO", nullable = false, length = 100)
    private String nomeProduto;

    @Column(name = "QUANTIDADE", nullable = false, length = 255)
    private int quantidade;

    @Column(name = "PRECO_UNITARIO", nullable = false, precision = 19, scale = 2)
    private BigDecimal precoUnitario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEDIDO_ID")
    private Pedido pedido;

}
