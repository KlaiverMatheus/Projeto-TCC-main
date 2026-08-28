package by.gabriel.gerenciadorEstoque.Exception.Usuario;

//Execeção lançada quando a senha do usuário está invalida
public class InvalidPasswordException extends RuntimeException {
    
    public InvalidPasswordException(String mensagem) {
        super(mensagem);
    }
}
