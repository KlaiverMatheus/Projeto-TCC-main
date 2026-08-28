package by.gabriel.gerenciadorEstoque.Exception.Produto;

public class HigherCostToSellException extends RuntimeException {

    public HigherCostToSellException(String message) {
        super(message);
    }
}
