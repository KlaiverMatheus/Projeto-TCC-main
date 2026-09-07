import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClienteSelectDTO } from '../models/cliente-select-dto';

@Injectable({
  providedIn: 'root',
})
export class AuthGerenciadorCliente {
  private apiUrl = 'http://localhost:8080/gerenciador-cliente';

  constructor(private http: HttpClient) {}

  listarClientes(): Observable<ClienteSelectDTO[]>{
    return this.http.get<ClienteSelectDTO[]>(`${this.apiUrl}/listCliente`);
  }

  inativarCliente(id: string, usuarioLogado: string): Observable<any> {
    const headers = new HttpHeaders({
      'X-Usuario-Logado': usuarioLogado
    });

    return this.http.patch<any>(`${this.apiUrl}/delete/${id}`, {}, { headers: headers });
  }

  atualizarCliente(id: string, nome: string, sobrenome: string, email: string, telefone: string, usuarioLogado: string): Observable<any> {
    const dadosAtualizacao = {
      nome: nome,
      sobrenome: sobrenome,
      email: email,
      telefone: telefone
    };

    const headers = new HttpHeaders({
      'X-Usuario-Logado': usuarioLogado
    });

    return this.http.patch<any>(`${this.apiUrl}/update/${id}`, dadosAtualizacao, { headers: headers });
  }
}
