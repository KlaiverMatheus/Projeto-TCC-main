package by.gabriel.gerenciadorEstoque.Repository.Pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.gabriel.gerenciadorEstoque.Model.Pedido.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
