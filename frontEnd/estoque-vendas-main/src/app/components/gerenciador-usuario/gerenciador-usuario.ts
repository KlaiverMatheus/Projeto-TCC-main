import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { AuthGerenciadorUsuario } from '../../services/auth-gerenciador-usuario';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { UserSelectDTO } from '../../models/user-select.dto';
import { NgxMaskDirective } from 'ngx-mask';
import { AuthNotificacaoService } from '../../services/auth-notificacao';

@Component({
  selector: 'app-gerenciador-usuario',
  standalone: true,
  imports: [FormsModule, CommonModule, NgxMaskDirective],
  templateUrl: './gerenciador-usuario.html',
  styleUrl: './gerenciador-usuario.css',
})
export class GerenciadorUsuario implements OnInit {
  idSelecionado: string = '';
  nomeDigitado: string = '';
  senhaDigitada: string = '';
  emailDigitado: string = '';
  telefoneDigitado: string = '';
  cargoDigitado: string = '';

  listaUsuarios: UserSelectDTO[] = [];

  carregando: boolean = false;

  constructor(
    private AuthGerenciadorUsuarioService: AuthGerenciadorUsuario,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private AuthNotificacaoService: AuthNotificacaoService
  ) {}

  get podeSalvar(): boolean {
    return (
      !!this.idSelecionado &&
      this.nomeDigitado.trim().length > 0 &&
      this.cargoDigitado.trim().length > 0
    );
  }

  ngOnInit(): void {
    this.carregarListaUsuarios();
  }

  carregarListaUsuarios(): void {
    this.AuthGerenciadorUsuarioService.listarUsuarios().subscribe({
      next: (dados) => {
        this.listaUsuarios = dados;
        this.cdr.detectChanges();
      },
      error: (erro) => console.error('Erro ao buscar usuários', erro)
    });
  }

  selecionarUsuario(usuario: UserSelectDTO): void {
    this.idSelecionado = usuario.userId;
    this.nomeDigitado = usuario.nome;
    this.emailDigitado = usuario.email;
    this.telefoneDigitado = usuario.telefone || '';
    this.cargoDigitado = usuario.cargo ? usuario.cargo.toUpperCase() : '';
    this.senhaDigitada = '';
  }

  inativarUsuario(usuario: UserSelectDTO, event: Event): void {
    event.stopPropagation();

    const confirmacao = confirm(`Tem certeza que deseja inativar o usuário ${usuario.nome}?`);

    if (!confirmacao) {
      return;
    }

    const usuarioAtual = localStorage.getItem('usuarioLogado') || 'Sistema';
    this.AuthGerenciadorUsuarioService.inativarUsuario(usuario.userId, usuarioAtual).subscribe({
      next: (resposta) => {
        this.AuthNotificacaoService.sucesso(resposta?.mensagem || `Usuário ${usuario.nome} inativado com sucesso!`);
        // pequeno atraso para dar tempo do back-end refletir a inativação antes de recarregar a lista
        setTimeout(() => {
          this.carregarListaUsuarios();
        }, 300);
      },
      error: (erro: HttpErrorResponse) => {
        const mensagem = this.AuthNotificacaoService.extrairMensagemErro(erro, 'Não foi possível inativar o usuário.');
        this.AuthNotificacaoService.erro(mensagem);
      }
    });
  }

  executarGerenciadorUsuario(): void {
    if (this.carregando) {
      return;
    }
    if (!this.podeSalvar) {
      this.AuthNotificacaoService.erro('Selecione um usuário na lista e preencha os campos obrigatórios antes de salvar.');
      return;
    }
    this.carregando = true;

    const usuarioAtual = localStorage.getItem('usuarioLogado') || 'Sistema';

    this.AuthGerenciadorUsuarioService.atualizarUsuario(
      this.idSelecionado, this.nomeDigitado, this.senhaDigitada, this.emailDigitado, this.telefoneDigitado,
      this.cargoDigitado, usuarioAtual
    ).subscribe({
      next: (resposta) => {
        this.carregando = false;
        this.cdr.detectChanges();
        this.AuthNotificacaoService.sucesso(resposta?.mensagem || 'Atualização realizada com sucesso');
        setTimeout(() => {
          this.carregarListaUsuarios();
        }, 300);
      },
      error: (erro: HttpErrorResponse) => {
        this.carregando = false;
        this.cdr.detectChanges();
        const mensagem = this.AuthNotificacaoService.extrairMensagemErro(erro, 'Atualização de usuário não foi possível');
        this.AuthNotificacaoService.erro(mensagem);
      }
    });
  }
}