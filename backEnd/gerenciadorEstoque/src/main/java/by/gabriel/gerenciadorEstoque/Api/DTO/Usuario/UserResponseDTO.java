package by.gabriel.gerenciadorEstoque.Api.DTO.Usuario;

import by.gabriel.gerenciadorEstoque.Enum.Usuario.UserCargo;
import by.gabriel.gerenciadorEstoque.Enum.Usuario.UserStatus;

//O que eu devolvo para o FrontEnd
public record UserResponseDTO(
    String nome,
    String email,
    String telefone,
    UserCargo userCargo,
    UserStatus userStatus
) {}

