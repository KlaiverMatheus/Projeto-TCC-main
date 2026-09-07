package by.gabriel.gerenciadorEstoque.Api.DTO.Pedido.Consultas;

import by.gabriel.gerenciadorEstoque.Enum.Pedido.PedidoStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoListDTO(
        Long id,
        String nomeCliente,
        BigDecimal valorTotal,
        PedidoStatus status,
        LocalDateTime dataVenda
) {}
