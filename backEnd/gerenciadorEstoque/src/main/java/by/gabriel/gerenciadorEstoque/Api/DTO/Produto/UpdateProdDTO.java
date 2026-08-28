package by.gabriel.gerenciadorEstoque.Api.DTO.Produto;

import by.gabriel.gerenciadorEstoque.Enum.Produto.ProdStatus;
import java.math.BigDecimal;

public record UpdateProdDTO(

    String nome,
    String marca,
    String codBarra,
    Integer quantidade,
    BigDecimal precoCusto,
    BigDecimal precoVenda,
    ProdStatus prodStatus

) {
}
