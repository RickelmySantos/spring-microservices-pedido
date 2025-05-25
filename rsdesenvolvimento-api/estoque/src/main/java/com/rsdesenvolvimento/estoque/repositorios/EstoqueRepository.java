package com.rsdesenvolvimento.estoque.repositorios;

import com.rsdesenvolvimento.estoque.modelo.entidade.Estoque;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
  List<Estoque> findByCategoria(String categoria);
}
