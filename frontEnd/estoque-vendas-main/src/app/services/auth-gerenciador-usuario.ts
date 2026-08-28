import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserSelectDTO } from '../models/user-select.dto';



@Injectable({
  providedIn: 'root',
})
export class AuthGerenciadorUsuario {
  private apiUrl = 'http://localhost:8080/usuario'

  constructor(private http: HttpClient) {}

  // metodo para buscar a lista de usuarios para a tabela
  listarUsuarios(): Observable<UserSelectDTO[]> {
    return this.http.get<UserSelectDTO[]>(`${this.apiUrl}/listAllUser`);
  }

  // metodo para inatviar
  inativarUsuario(userId: string, usuarioLogado: string): Observable<any> {
  const headers = new HttpHeaders({
    'X-Usuario-Logado': usuarioLogado
  });

  // Envia a requisiçao para inativar
  return this.http.patch<any>(`${this.apiUrl}/delete/${userId}`, {}, { headers: headers });
  }

  // Novo método para atualizar o usuário usando o endpoint correto de patch
  atualizarUsuario(userId: string, nome: string, senha: string, email: any, telefone: string, userCargo: string, usuarioLogado: string): Observable<any> {
    const dadosAtualizacao = {
      nome: nome,
      senha: senha,
      email: email,
      telefone: telefone,
      userCargo: userCargo
    };
    
    const headers = new HttpHeaders({
      'X-Usuario-Logado': usuarioLogado
    });

    return this.http.patch<any>(`${this.apiUrl}/update/${userId}`, dadosAtualizacao, { headers: headers });
  }
}