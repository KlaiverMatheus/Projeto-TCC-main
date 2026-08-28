import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, DestroyRef, OnInit, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { SelectFormPag } from '../../models/forma-pagamento-dto';
import { AuthFormaPagamento } from '../../services/auth-forma-pagamento';
import { AuthNotificacaoService } from '../../services/auth-notificacao';

@Component({
  selector: 'app-forma-pagamento',
  imports: [FormsModule, CommonModule],
  templateUrl: './forma-pagamento.html',
  styleUrl: './forma-pagamento.css',
})
export class FormaPagamento implements OnInit {
  private readonly destroyRef = inject(DestroyRef);

  descricaoDigitado: string = '';

  listarFormaPag: SelectFormPag[] = [];

  carregando: boolean = false;

  inativando: Set<string> = new Set();

  constructor(
    private authFormaPagamentoService: AuthFormaPagamento,
    private cdr: ChangeDetectorRef,
    private AuthNotificacaoService: AuthNotificacaoService
  ) {}

  ngOnInit(): void {
    this.carregarListaFormaPagamento();
  }

  carregarListaFormaPagamento(): void {
    this.authFormaPagamentoService
      .listarFormaPagamento()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (dados) => {
          this.listarFormaPag = dados;
          this.cdr.detectChanges();
        },
        error: (erro) => {
          console.error('Erro ao buscar as formas de pagamento', erro);
          this.listarFormaPag = []; // evita manter dados desatualizados na tela em caso de erro
          this.cdr.detectChanges();
        },
      });
  }

  inativarFormaPagamento(formaPagamento: SelectFormPag, event: Event): void {
    event.stopPropagation();

    if (this.inativando.has(formaPagamento.descricao)) {
      return;
    }

    const confirmacao = confirm(
      `Tem certeza que quer inativar essa forma de pagamento ${formaPagamento.descricao}?`
    );

    if (!confirmacao) {
      return;
    }

    this.inativando.add(formaPagamento.descricao);

    const usuarioAtual = localStorage.getItem('usuarioLogado') || 'Sistema';

    this.authFormaPagamentoService
      .inativarFormaPagamento(formaPagamento.descricao, usuarioAtual)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (resposta) => {
          this.AuthNotificacaoService.sucesso(resposta?.mensagem || `Forma de pagamento ${formaPagamento.descricao} inativada com sucesso!`);
          this.inativando.delete(formaPagamento.descricao);
          this.cdr.detectChanges();
          // dá tempo do back-end concluir antes de recarregar a lista
          setTimeout(() => {
            this.cdr.detectChanges();
          }, 300);
          setTimeout(() => {
            this.carregarListaFormaPagamento();
          }, 300);
        },
        error: (erro: HttpErrorResponse) => {
          const mensagem = this.AuthNotificacaoService.extrairMensagemErro(erro, 'Não foi possível inativar');
          this.AuthNotificacaoService.erro(mensagem);
          this.inativando.delete(formaPagamento.descricao);
          this.cdr.detectChanges();
        },
      });
  }

  isInativando(formaPagamento: SelectFormPag): boolean {
    return this.inativando.has(formaPagamento.descricao);
  }

  executarFormaPagamento(): void {
    if (this.carregando) {
      return;
    }

    const descricao = this.descricaoDigitado.trim();
    if (!descricao) {
      return;
    }

    this.carregando = true;

    const usuarioAtual = localStorage.getItem('usuarioLogado') || 'Sistema';

    this.authFormaPagamentoService
      .cadastroFormaPagamento(descricao, usuarioAtual)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (resposta) => {
          this.carregando = false;
          this.descricaoDigitado = '';
          this.cdr.detectChanges();
          this.AuthNotificacaoService.sucesso(resposta?.mensagem || 'Cadastro realizado com sucesso');
          // dá tempo do back-end concluir antes de recarregar a lista
          setTimeout(() => {
            this.carregarListaFormaPagamento();
          }, 300);
        },
        error: (erro: HttpErrorResponse) => {
          this.carregando = false;
          this.cdr.detectChanges();
          const mensagem = this.AuthNotificacaoService.extrairMensagemErro(erro, 'Cadastro não foi possível');
          this.AuthNotificacaoService.erro(mensagem);
        },
      });
  }

  trackByDescricao(_index: number, item: SelectFormPag): string {
    return item.descricao;
  }
}