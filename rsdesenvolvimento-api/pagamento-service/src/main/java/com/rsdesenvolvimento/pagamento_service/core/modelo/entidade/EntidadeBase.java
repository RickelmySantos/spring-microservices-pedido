package com.rsdesenvolvimento.pagamento_service.core.modelo.entidade;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * EntidadeBase
 *
 * @author rsdesenvolvimento
 * @version 1.0
 * @since 2025-04-29
 */
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@FieldNameConstants
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class EntidadeBase implements Comparable<EntidadeBase> {

    private static final long serialVersionUID = 1L;

    public static final String SEQUENCE_GENERATOR = "sequence";


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = EntidadeBase.SEQUENCE_GENERATOR)
    @Column(name = "ID", nullable = false, unique = true)
    @Positive
    @ToString.Include
    @EqualsAndHashCode.Include
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime dataHoraCriacao;

    @CreatedBy
    @Column(updatable = false)
    private String criadoPor;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime dataHoraAtualizacao;

    @LastModifiedBy
    @Column(insertable = false)
    private String atualizadoPor;



    @Override
    public int compareTo(EntidadeBase entidade) {
        if (this.id == entidade.id) {
            return 0;
        }

        if (this.id == null && entidade.id != null) {
            return -1;
        }

        if (this.id != null && entidade.id == null) {
            return 1;
        }

        return this.id.compareTo(entidade.id);
    }
}
