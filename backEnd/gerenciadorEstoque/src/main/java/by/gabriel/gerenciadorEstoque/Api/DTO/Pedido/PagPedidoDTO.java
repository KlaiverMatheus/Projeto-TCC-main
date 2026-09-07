package by.gabriel.gerenciadorEstoque.Api.DTO.Pedido;

import java.math.BigDecimal;

public record PagPedidoDTO(

    Long formaPagId,
    BigDecimal valorPago

) {

}
