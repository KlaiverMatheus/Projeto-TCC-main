package by.gabriel.gerenciadorEstoque.Api.DTO.Venda;

import java.math.BigDecimal;

public record PagVendaDTO(

    Long formaPagId,
    BigDecimal valorPago

) {

}
