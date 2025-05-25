package com.rsdesenvolvimento.pedido_service.repositorios;

import com.rsdesenvolvimento.pedido_service.modelo.entidades.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
