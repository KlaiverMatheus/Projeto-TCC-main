package by.gabriel.gerenciadorEstoque.Repository.Cliente;

import by.gabriel.gerenciadorEstoque.Model.Cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
