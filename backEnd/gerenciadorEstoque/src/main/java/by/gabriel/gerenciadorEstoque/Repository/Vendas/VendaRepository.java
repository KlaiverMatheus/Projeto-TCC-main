package by.gabriel.gerenciadorEstoque.Repository.Vendas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.gabriel.gerenciadorEstoque.Model.Vendas.Venda;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {

}
