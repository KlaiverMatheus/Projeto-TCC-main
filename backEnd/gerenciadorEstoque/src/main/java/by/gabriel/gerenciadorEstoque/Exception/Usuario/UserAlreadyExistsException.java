package by.gabriel.gerenciadorEstoque.Exception.Usuario;


public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String mensagem) {
        super(mensagem);
    }
}
