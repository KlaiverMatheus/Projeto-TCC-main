package by.gabriel.gerenciadorEstoque.Model.Vendas;

import java.math.BigDecimal;

import by.gabriel.gerenciadorEstoque.Model.FormaPag.FormaPagto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PagVenda {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;

    @ManyToOne
    @JoinColumn(name = "forma_pagto_id", nullable = false)
    private FormaPagto formaPagto;

    private BigDecimal valorPago;

    public PagVenda() {
    }

    public PagVenda(Long id, Venda venda, FormaPagto formaPagto, BigDecimal valorPago) {
        this.id = id;
        this.venda = venda;
        this.formaPagto = formaPagto;
        this.valorPago = valorPago;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public FormaPagto getFormaPagto() {
        return formaPagto;
    }

    public void setFormaPagto(FormaPagto formaPagto) {
        this.formaPagto = formaPagto;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }



}
