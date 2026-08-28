import { Component, ChangeDetectorRef } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Auth } from '../../services/auth';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthNotificacaoService } from '../../services/auth-notificacao';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  usuarioDigitado: string = '';
  senhaDigitada: string = '';

  carregando: boolean = false;

  constructor(
    private Auth: Auth,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private authNotificacaoService: AuthNotificacaoService
  ) {}

  executarLogin(): void {
    if (this.carregando) {
      return;
    }

    if (!this.usuarioDigitado || !this.senhaDigitada) {
      this.authNotificacaoService.erro('Preencha todos os campos!');
      return;
    }

    this.carregando = true;

    this.Auth.login(this.usuarioDigitado, this.senhaDigitada).subscribe({
      next: (resposta) => {
        localStorage.setItem('usuarioLogado', this.usuarioDigitado);

        // mostra a mensagem que o back-end realmente mandou (ex: "Usuario logado com sucesso")
        this.authNotificacaoService.sucesso(resposta?.mensagem || 'Login realizado com sucesso!');

        this.carregando = false;
        this.cdr.detectChanges();
        this.router.navigate(['/dashboard']);
      },
      error: (erro: HttpErrorResponse) => {
        // mostra a mensagem que o back-end mandou (ex: "Nome ou senha inválidos")
        const mensagem = this.authNotificacaoService.extrairMensagemErro(erro, 'Usuário ou senha incorretos!');
        this.authNotificacaoService.erro(mensagem);
        this.carregando = false;
        this.cdr.detectChanges();
      }
    });
  }
}