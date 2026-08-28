package by.gabriel.gerenciadorEstoque.Model.FormaPag;

import by.gabriel.gerenciadorEstoque.Enum.FormaPag.FormaPagStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FormaPagto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private FormaPagStatus status;

    public FormaPagto(){

    }

    public FormaPagto(String descricao, FormaPagStatus status) {

        this.descricao = descricao;
        this.status = status;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public FormaPagStatus getStatus() {
        return status;
    }

    public void setStatus(FormaPagStatus status) {
        this.status = status;
    }
}
