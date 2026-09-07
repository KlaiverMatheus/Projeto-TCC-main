package by.gabriel.gerenciadorEstoque.Api.DTO.Servicos;

import by.gabriel.gerenciadorEstoque.Enum.Servicos.ServicosStatus;
import java.math.BigDecimal;

public record ServicosDTO(

        String descServico,
        BigDecimal precoServico,
        ServicosStatus status

) {
}
