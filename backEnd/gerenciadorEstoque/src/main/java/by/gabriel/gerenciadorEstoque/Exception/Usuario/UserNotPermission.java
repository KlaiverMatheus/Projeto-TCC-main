package by.gabriel.gerenciadorEstoque.Exception.Usuario;

//Excessão para usuarios sem permissão
public class UserNotPermission extends RuntimeException{

    public UserNotPermission(String mensagem) {
        super(mensagem);
    }
}
