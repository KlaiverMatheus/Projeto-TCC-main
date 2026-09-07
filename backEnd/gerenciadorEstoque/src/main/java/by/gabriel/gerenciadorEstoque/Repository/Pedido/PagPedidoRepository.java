package by.gabriel.gerenciadorEstoque.Repository.Pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.gabriel.gerenciadorEstoque.Model.Pedido.PagPedido;

@Repository
public interface PagPedidoRepository extends JpaRepository<PagPedido, Long> {

}
