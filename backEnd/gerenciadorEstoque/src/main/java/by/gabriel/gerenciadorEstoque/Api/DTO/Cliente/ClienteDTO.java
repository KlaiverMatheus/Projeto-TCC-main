package by.gabriel.gerenciadorEstoque.Api.DTO.Cliente;

import by.gabriel.gerenciadorEstoque.Enum.Cliente.ClienteStatus;

public record ClienteDTO(
        String nome,
        String sobrenome,
        String email,
        String telefone,
        ClienteStatus status
) {
}
