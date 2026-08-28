package by.gabriel.gerenciadorEstoque.Exception.Produto;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
