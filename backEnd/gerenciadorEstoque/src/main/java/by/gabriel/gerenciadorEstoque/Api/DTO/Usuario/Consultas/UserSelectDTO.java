package by.gabriel.gerenciadorEstoque.Api.DTO.Usuario.Consultas;

import by.gabriel.gerenciadorEstoque.Enum.Usuario.UserCargo;
import by.gabriel.gerenciadorEstoque.Enum.Usuario.UserStatus; // Importe o Enum
import java.util.UUID;

public record UserSelectDTO(
        UUID userId,
        String nome,
        String email,
        UserCargo cargo,   // Deve bater com u.userCargo na query
        UserStatus status,
        String telefone// Deve bater com u.userStatus na query
) {
}