package by.gabriel.gerenciadorEstoque.Exception.Usuario;

// Exceção lançada quando o usuário está inativo
public class UserInactiveException extends RuntimeException {
    
    public UserInactiveException(String mensagem) {
        super(mensagem);
    }
}


