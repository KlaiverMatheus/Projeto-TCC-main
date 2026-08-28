package by.gabriel.gerenciadorEstoque.Repository.Vendas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.gabriel.gerenciadorEstoque.Model.Vendas.PagVenda;

@Repository
public interface PagVendaRepository extends JpaRepository<PagVenda, Long> {

}
