import { Component, OnInit } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Sidebar } from './components/sidebar/sidebar';
import { NotificacaoToast } from './components/notificacao-toast/notificacao-toast';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, Sidebar, NotificacaoToast],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  mostrarSidebar: boolean = false;

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        const rotaAtual = event.urlAfterRedirects;
        
        // Esconde a Sidebar apenas se a rota for de login/vazia
        const rotasSemSidebar = ['/', '', '/login'];
        this.mostrarSidebar = !rotasSemSidebar.includes(rotaAtual);
      }
    });
  }
}