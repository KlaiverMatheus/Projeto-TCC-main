package by.gabriel.gerenciadorEstoque.Api.DTO.Servicos.Consultas;

import by.gabriel.gerenciadorEstoque.Enum.Servicos.ServicosStatus;
import java.math.BigDecimal;

public record ServicosSelectDTO(

        Long servicosId,
        String descServico,
        BigDecimal precoServico,
        ServicosStatus status

) {
}
