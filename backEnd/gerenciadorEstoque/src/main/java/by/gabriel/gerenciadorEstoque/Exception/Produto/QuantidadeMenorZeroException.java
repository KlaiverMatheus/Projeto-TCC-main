package by.gabriel.gerenciadorEstoque.Exception.Produto;

public class QuantidadeMenorZeroException extends RuntimeException {
    public QuantidadeMenorZeroException(String message) {
        super(message);
    }
}
