package by.gabriel.gerenciadorEstoque.Api.DTO.FormPagDTO;

import by.gabriel.gerenciadorEstoque.Enum.FormaPag.FormaPagStatus;

public record FormPagDTO(

        String descricao,
        FormaPagStatus status ) {
}
