package by.gabriel.gerenciadorEstoque.Exception.Cliente;

public class EmailClienteJaExiste extends RuntimeException {
    public EmailClienteJaExiste(String message) {
        super(message);
    }
}
