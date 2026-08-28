package by.gabriel.gerenciadorEstoque.Exception.Usuario;

//Exceção de quando o email já existe
public class EmailAlreadyExistException extends RuntimeException{
    
    public EmailAlreadyExistException(String mensagem) {
        super(mensagem);
    }
}
