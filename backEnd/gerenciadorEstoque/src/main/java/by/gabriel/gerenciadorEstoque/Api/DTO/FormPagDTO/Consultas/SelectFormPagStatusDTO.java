package by.gabriel.gerenciadorEstoque.Api.DTO.FormPagDTO.Consultas;

import by.gabriel.gerenciadorEstoque.Enum.FormaPag.FormaPagStatus;

public record SelectFormPagStatusDTO(

        Long id,
        String descricao,
        FormaPagStatus status) {
}
