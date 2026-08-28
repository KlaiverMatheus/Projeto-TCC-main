package by.gabriel.gerenciadorEstoque.Api.DTO.Usuario;


import by.gabriel.gerenciadorEstoque.Enum.Usuario.UserCargo;

//DTO usado para realizar updates
public record UpdateUserDTO(
    String nome,
    String email,
    String senha,
    String telefone,
    UserCargo userCargo) {
}