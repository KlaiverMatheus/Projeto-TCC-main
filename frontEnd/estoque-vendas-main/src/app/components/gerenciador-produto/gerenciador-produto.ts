import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { SelectAllProdDTO } from '../../models/select-all-prod-dto';
import { AuthGerenciadorProduto } from '../../services/auth-gerenciador-produto';
import { Router } from '@angular/router';
import { AuthNotificacaoService } from '../../services/auth-notificacao';

@Component({
  selector: 'app-gerenciador-produto',
  imports: [FormsModule, CommonModule],
  templateUrl: './gerenciador-produto.html',
  styleUrl: './gerenciador-produto.css',
})
export class GerenciadorProduto implements OnInit {
  prodIdSelecionado: string = '';
  nomeDigitado: string = '';
  marcaDigitada: string = '';
  codBarraDigitada: string = '';
  quantidadeDigitada: number = 0;
  precoCustoDigitado: number = 0;
  precoVendaDigitado: number = 0;

  carregando: boolean = false;

  listaProdutos: SelectAllProdDTO[] = [];

  constructor(
    private AuthGerenciadorProdutoService: AuthGerenciadorProduto,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private AuthNotificacaoService: AuthNotificacaoService
  ) {}

  get podeSalvar(): boolean {
    return (
      !!this.prodIdSelecionado &&
      this.nomeDigitado.trim().length > 0 &&
      this.marcaDigitada.trim().length > 0 &&
      this.quantidadeDigitada != null &&
      this.precoCustoDigitado != null &&
      this.precoVendaDigitado != null
    );
  }

  ngOnInit(): void {
    this.carregarListaProdutos();
  }

  carregarListaProdutos(): void {
    this.AuthGerenciadorProdutoService.listarProdutos().subscribe({
      next: (dados) => {
        this.listaProdutos = dados;
        this.cdr.detectChanges();
      },
      error: (erro) => console.error('Erro ao buscar produtos', erro)
    });
  }

  selecionarProduto(produto: SelectAllProdDTO): void {
    this.prodIdSelecionado = produto.prodId;
    this.nomeDigitado = produto.nome;
    this.marcaDigitada = produto.marca;
    this.codBarraDigitada = produto.codBarra || '';
    this.quantidadeDigitada = produto.quantidade;
    this.precoCustoDigitado = produto.precoCusto;
    this.precoVendaDigitado = produto.precoVenda;
  }

  inativarProduto(produto: SelectAllProdDTO, event: Event): void {
    event.stopPropagation();

    const confirmacao = confirm(`Tem certeza que deseja inativar o produto ${produto.nome}?`);

    if (!confirmacao) {
      return;
    }

    const usuarioAtual = localStorage.getItem('usuarioLogado') || 'Sistema';
    this.AuthGerenciadorProdutoService.inativarProduto(produto.prodId, usuarioAtual).subscribe({
      next: (resposta) => {
        this.AuthNotificacaoService.sucesso(resposta?.mensagem || `Produto ${produto.nome} inativado com sucesso!`);
        setTimeout(() => {
          this.carregarListaProdutos();
        }, 300);
      },
      error: (erro: HttpErrorResponse) => {
        const mensagem = this.AuthNotificacaoService.extrairMensagemErro(erro, 'Não foi possível inativar o produto.');
        this.AuthNotificacaoService.erro(mensagem);
      }
    });
  }

  executarGerenciadorProduto(): void {
    if (this.carregando) {
      return;
    }
    if (!this.podeSalvar) {
      this.AuthNotificacaoService.erro('Selecione um produto na lista e preencha os campos obrigatórios antes de salvar.');
      return;
    }
    this.carregando = true;

    const usuarioAtual = localStorage.getItem('usuarioLogado') || 'Sistema';

    this.AuthGerenciadorProdutoService.atualizarProduto(
      this.prodIdSelecionado, this.nomeDigitado, this.marcaDigitada, this.codBarraDigitada, this.quantidadeDigitada,
      this.precoCustoDigitado, this.precoVendaDigitado, usuarioAtual
    ).subscribe({
      next: (resposta) => {
        this.carregando = false;
        this.cdr.detectChanges();
        this.AuthNotificacaoService.sucesso(resposta?.mensagem || 'Atualização realizada com sucesso');
        setTimeout(() => {
          this.carregarListaProdutos();
        }, 300);
      },
      error: (erro: HttpErrorResponse) => {
        this.carregando = false;
        this.cdr.detectChanges();
        const mensagem = this.AuthNotificacaoService.extrairMensagemErro(erro, 'Atualização de produto não foi possível');
        this.AuthNotificacaoService.erro(mensagem);
      }
    });
  }
}