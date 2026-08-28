package by.gabriel.gerenciadorEstoque.Api.DTO.Usuario;

import by.gabriel.gerenciadorEstoque.Enum.Usuario.UserCargo;

//O que eu recebo do FrontEnd
public record UserDTO(
    String senha,
    String nome,
    String email,
    UserCargo userCargo,
    String telefone) {

}
