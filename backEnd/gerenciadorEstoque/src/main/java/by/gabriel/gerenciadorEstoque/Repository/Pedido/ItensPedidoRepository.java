package by.gabriel.gerenciadorEstoque.Repository.Pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import by.gabriel.gerenciadorEstoque.Model.Pedido.ItensPedido;

@Repository
public interface ItensPedidoRepository extends JpaRepository<ItensPedido, Long> {

}
