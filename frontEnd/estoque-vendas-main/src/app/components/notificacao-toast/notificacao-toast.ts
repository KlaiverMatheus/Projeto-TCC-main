import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthNotificacaoService } from '../../services/auth-notificacao';

@Component({
  selector: 'app-notificacao-toast',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notificacao-toast.html',
  styleUrl: './notificacao-toast.css',
})
export class NotificacaoToast {
  constructor(public authNotificacaoService: AuthNotificacaoService) {}
}