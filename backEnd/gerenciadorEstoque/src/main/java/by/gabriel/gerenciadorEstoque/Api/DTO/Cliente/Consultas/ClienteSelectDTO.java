package by.gabriel.gerenciadorEstoque.Api.DTO.Cliente.Consultas;

import by.gabriel.gerenciadorEstoque.Enum.Cliente.ClienteStatus;

public record ClienteSelectDTO(
        Long id,
        String nome,
        String sobrenome,
        String email,
        String telefone,
        ClienteStatus status
) {}
