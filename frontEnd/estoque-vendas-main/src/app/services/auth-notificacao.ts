import { Injectable, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

export type TipoNotificacao = 'sucesso' | 'erro';

export interface Notificacao {
  tipo: TipoNotificacao;
  mensagem: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthNotificacaoService {
  // signal que o toast vai "ouvir" pra saber quando mostrar algo
  notificacaoAtual = signal<Notificacao | null>(null);

  sucesso(mensagem: string): void {
    this.exibir('sucesso', mensagem);
  }

  erro(mensagem: string): void {
    this.exibir('erro', mensagem);
  }

  /**
   * Extrai a mensagem que o back-end mandou dentro do ResponseDTO (campo "mensagem").
   * Se não conseguir (ex: back-end fora do ar, erro de rede), usa uma mensagem padrão.
   */
  extrairMensagemErro(erro: HttpErrorResponse, mensagemPadrao = 'Ocorreu um erro. Tente novamente.'): string {
    if (erro?.error?.mensagem) {
      return erro.error.mensagem;
    }
    return mensagemPadrao;
  }

  private exibir(tipo: TipoNotificacao, mensagem: string): void {
    this.notificacaoAtual.set({ tipo, mensagem });

    // some sozinho depois de 4 segundos
    setTimeout(() => {
      // só limpa se ainda for essa mesma notificação (evita apagar uma mais nova)
      if (this.notificacaoAtual()?.mensagem === mensagem) {
        this.notificacaoAtual.set(null);
      }
    }, 4000);
  }

  fechar(): void {
    this.notificacaoAtual.set(null);
  }
}