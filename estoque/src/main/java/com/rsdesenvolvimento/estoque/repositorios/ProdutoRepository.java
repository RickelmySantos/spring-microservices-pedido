package com.rsdesenvolvimento.estoque.repositorios;

import com.rsdesenvolvimento.estoque.modelo.entidade.Produto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
  List<Produto> findByCategoria(String categoria);
}
