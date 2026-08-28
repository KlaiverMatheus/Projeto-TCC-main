import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = () => {
  const router = inject(Router);

  // Verifica se existe um usuário logado salvo no navegador
  const usuarioLogado = localStorage.getItem('usuarioLogado');

  if (usuarioLogado) {
    return true; // pode acessar a rota
  }

  // Não está logado: manda de volta pro login e bloqueia o acesso
  router.navigate(['']);
  return false;
};