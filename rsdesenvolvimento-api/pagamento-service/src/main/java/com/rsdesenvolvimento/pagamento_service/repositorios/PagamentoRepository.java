package com.rsdesenvolvimento.pagamento_service.repositorios;

import com.rsdesenvolvimento.pagamento_service.modelo.entidades.Pagamento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByStatus(String status);

}
