package by.gabriel.gerenciadorEstoque.Api.DTO.Venda;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record VendaDTO(

    UUID usuarioId,
    String cliente,
    BigDecimal desconto,
    List<ItensVendaDTO> itensVenda,
    List<PagVendaDTO> pagVenda

) {

}
