package by.gabriel.gerenciadorEstoque.Api.DTO.Produto;

import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;

public record ProdutoDTO(
        String nome,
        String marca,
        String codBarra,
        Integer quantidade,
        BigDecimal precoCusto,
        BigDecimal precoVenda
) {
}
