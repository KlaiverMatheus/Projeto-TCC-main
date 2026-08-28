package by.gabriel.gerenciadorEstoque.Exception.Produto;

public class ProdNotFoundException extends RuntimeException {
    public ProdNotFoundException(String message) {
        super(message);
    }
}
