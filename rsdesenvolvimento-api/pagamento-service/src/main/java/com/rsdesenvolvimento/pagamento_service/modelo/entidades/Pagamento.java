package com.rsdesenvolvimento.pagamento_service.modelo.entidades;

import com.rsdesenvolvimento.pagamento_service.core.modelo.entidade.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@SequenceGenerator(name = EntidadeBase.SEQUENCE_GENERATOR, sequenceName = "SQ_PAGAMENTO",
        initialValue = 1, allocationSize = 1)
@Table(name = "TB_PAGAMENTO", uniqueConstraints = {})
public class Pagamento extends EntidadeBase {

    @Column(name = "PEDIDO_ID", nullable = false)
    private Long pedidoId;
    @Column(name = "VALOR", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    @Column(name = "DATA_HORA", nullable = false)
    private LocalDateTime dataHora;
    @Column(name = "STATUS", nullable = false, length = 20)
    private String status;
}
