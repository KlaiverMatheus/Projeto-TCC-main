package by.gabriel.gerenciadorEstoque.Api.DTO.Response;

import by.gabriel.gerenciadorEstoque.Enum.Venda.VendaStatus;
import java.math.BigDecimal;

public record VendaResponseDTO(
        Long vendaId,
        String cliente,
        BigDecimal valorTotal,
        VendaStatus status,
        String mensagem
) {}
