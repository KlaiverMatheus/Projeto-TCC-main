package by.gabriel.gerenciadorEstoque.Exception.Usuario;


//Exceção lança quando o Usuaro está nulo

public class UserPasswordNotNullException extends RuntimeException {
    public UserPasswordNotNullException(String mensagem) {
        super(mensagem);
    }
}

