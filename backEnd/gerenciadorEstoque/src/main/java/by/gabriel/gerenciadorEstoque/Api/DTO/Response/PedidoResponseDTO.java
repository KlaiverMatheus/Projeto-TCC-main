package by.gabriel.gerenciadorEstoque.Api.DTO.Response;

import by.gabriel.gerenciadorEstoque.Enum.Pedido.PedidoStatus;
import java.math.BigDecimal;

public record PedidoResponseDTO(
        Long vendaId,
        String nomeCliente, // <-- Deixei mais claro que é apenas o nome
        BigDecimal valorTotal,
        PedidoStatus status,
        String mensagem
) {}