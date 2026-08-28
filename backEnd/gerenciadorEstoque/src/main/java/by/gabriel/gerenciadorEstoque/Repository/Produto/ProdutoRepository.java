package by.gabriel.gerenciadorEstoque.Repository.Produto;

import by.gabriel.gerenciadorEstoque.Api.DTO.Produto.Consultas.SelectAllProdDTO;
import by.gabriel.gerenciadorEstoque.Model.Produto.Produto;
import by.gabriel.gerenciadorEstoque.Enum.Produto.ProdStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByNomeIgnoreCase(String nome);

    Optional<Produto> findByCodBarraIgnoreCase(String codBarra);

    @Query("""
            SELECT new by.gabriel.gerenciadorEstoque.Api.DTO.Produto.Consultas.SelectAllProdDTO(
                pd.prodId, 
                pd.nome,
                pd.marca,
                pd.codBarra,
                pd.quantidade,
                pd.precoCusto,
                pd.precoVenda,
                pd.prodStatus
            )
            FROM Produto pd 
            WHERE pd.prodStatus = :status 
            ORDER BY pd.nome ASC
            """)
    List<SelectAllProdDTO> findByStatusCustom(@Param("status") ProdStatus status);

}