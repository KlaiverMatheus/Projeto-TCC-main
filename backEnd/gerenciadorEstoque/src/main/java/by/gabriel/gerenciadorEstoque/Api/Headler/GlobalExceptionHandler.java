package by.gabriel.gerenciadorEstoque.Api.Headler;

import java.time.Instant;

import by.gabriel.gerenciadorEstoque.Api.DTO.Response.ResponseDTO;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(2)
public class GlobalExceptionHandler {

    //EXCEÇÃO GENERICA GLOBAL
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleGeneric(Exception ex) {

        System.out.println(ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ResponseDTO(false, "Erro inesperado", "INTERNAL_ERROR", Instant.now().toString()));
    }
}
