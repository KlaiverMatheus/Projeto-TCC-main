package by.gabriel.gerenciadorEstoque.Api.Headler.Prod;

import by.gabriel.gerenciadorEstoque.Api.DTO.Response.ResponseDTO;
import by.gabriel.gerenciadorEstoque.Exception.Produto.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
@Order(1)
public class ProdExceptionHandler {

    @ExceptionHandler(HigherCostToSellException.class)
    public ResponseEntity<ResponseDTO> handlerHigherCostToSell(HigherCostToSellException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false, ex.getMessage(), "HIGHER_COST_TO_SELL", Instant.now().toString()));
    }

    @ExceptionHandler(CostOrSellBellowZeroException.class)
    public ResponseEntity<ResponseDTO> handlerCostOrSellBellowZero(CostOrSellBellowZeroException ex) { // Corrigido: Tipo do parâmetro alterado para bater com a Exception
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false, ex.getMessage(), "COST_SELL_BELLOW_ZERO", Instant.now().toString()));
    }

    @ExceptionHandler(ProdNotFoundException.class)
    public ResponseEntity<ResponseDTO> handlerProdNotFound(ProdNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND) // Alterado para 404 NOT FOUND que é o padrão para recurso não encontrado
                .body(new ResponseDTO(false, ex.getMessage(), "PROD_NOT_FOUND", Instant.now().toString()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseDTO> handlerAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN) // Alterado para 403 FORBIDDEN para erros de permissão
                .body(new ResponseDTO(false, ex.getMessage(), "CARGO_INVALIDO", Instant.now().toString()));
    }

    @ExceptionHandler(QuantidadeMenorZeroException.class)
    public ResponseEntity<ResponseDTO> handlerQuantidadeMenorZeroException(QuantidadeMenorZeroException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false, ex.getMessage(), "QUANTIDADE_INVALIDA", Instant.now().toString()));
    }

    @ExceptionHandler(PrecosNotNullException.class)
    public ResponseEntity<ResponseDTO> handlerPrecosNotNull(PrecosNotNullException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false, "Preços não podem ser nulos", "PRECO_NULL", Instant.now().toString()));
    }

    @ExceptionHandler(CodBarraMenorException.class)
    public ResponseEntity<ResponseDTO> handlerCodBarraMenorException(CodBarraMenorException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false, "Código de barras deve ter ao menos 13 dígitos", "COD_BARRA_INVALIDO", Instant.now().toString()));
    }

    @ExceptionHandler(NomeProdVazioException.class)
    public ResponseEntity<ResponseDTO> handlerNomeProdVazioException(NomeProdVazioException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false, "Nome do produto não pode ser vazio", "NOME_NULL", Instant.now().toString()));
    }

    @ExceptionHandler(CodBarraVazioException.class)
    public ResponseEntity<ResponseDTO> handlerCodBarraVazioException(CodBarraVazioException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false, "Codigo de barras não pode ser vazio", "COD_BARRA_NULL", Instant.now().toString()));
    }

    @ExceptionHandler(ListaProdVaziaException.class)
    public ResponseEntity<ResponseDTO> handlerListaProdVaziaException(ListaProdVaziaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false, "Não possui nenhum produto cadastrado no sistema", "SEM_REGISTRO_BANCO", Instant.now().toString()));
    }

    @ExceptionHandler(NomeProdJaExistenteException.class)
    public ResponseEntity<ResponseDTO> handlerNomeProdJaExistenteException(NomeProdJaExistenteException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false, "Nome do produto já cadastrado", "NOME_PROD_EXISTENTE", Instant.now().toString()));
    }

    @ExceptionHandler(CodBarraExistenteException.class)
    public ResponseEntity<ResponseDTO> handlerCodBarraExistenteException(CodBarraExistenteException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false, "Código de barras já registrado.", "COD_BARRA_EXISTENTE", Instant.now().toString()));
    }

    @ExceptionHandler(MarcaNotExistException.class)
    public ResponseEntity<ResponseDTO> handlerMarcaNotExistException(MarcaNotExistException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false, "Marca do produto não existe no sistema", "MARCA_NOT_EXIST", Instant.now().toString()));
    }

    @ExceptionHandler(MarcaNotNullException.class)
    public ResponseEntity<ResponseDTO> handlerMarcaNotNullException(MarcaNotNullException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDTO(false, "Marca do produto não pode estar vazia", "MARCA_NOT_NULL", Instant.now().toString()));
    }

    //MarcaNotNullException
}