package by.gabriel.gerenciadorEstoque.Model.Pedido;

import java.math.BigDecimal;

import by.gabriel.gerenciadorEstoque.Model.FormaPag.FormaPagto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class PagPedido {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Pedido venda;

    @ManyToOne
    @JoinColumn(name = "forma_pagto_id", nullable = false)
    private FormaPagto formaPagto;

    private BigDecimal valorPago;

    public PagPedido() {
    }

    public PagPedido(Long id, Pedido venda, FormaPagto formaPagto, BigDecimal valorPago) {
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

    public Pedido getVenda() {
        return venda;
    }

    public void setVenda(Pedido venda) {
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
