package by.gabriel.gerenciadorEstoque.Api.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // só inclui no JSON se não for null
public class ResponseDTO {

    private Boolean sucesso;
    private String mensagem;
    private String codigoErro;
    private String timestamp;

    //Construtor para exceções de erro
    public ResponseDTO(Boolean sucesso, String mensagem, String codigoErro, String timestamp) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.codigoErro = codigoErro;
        this.timestamp = timestamp;
    }

    public ResponseDTO(Boolean sucesso, String mensagem, String timestamp) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.timestamp = timestamp;
    }



    public Boolean getSucesso() {
        return sucesso;
    }

    public void setSucesso(Boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getCodigoErro() {
        return codigoErro;
    }

    public void setCodigoErro(String codigoErro) {
        this.codigoErro = codigoErro;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
 

    
}
