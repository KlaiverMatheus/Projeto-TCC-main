import { Injectable } from '@angular/core';
import { ServicosDTO } from '../models/servicos-dto';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthGerenciadorServicos {

  private apiUrl = 'http://localhost:8080/gerenciador-servicos';

  constructor(private http: HttpClient) {}

  // Método para buscar a lista de serviços
  listarServicos(): Observable<ServicosDTO[]> {
    return this.http.get<ServicosDTO[]>(
      `${this.apiUrl}/listarServicos`
    );
  }

  // Método para inativar um serviço
  inativarServicos(
    servicosId: string,
    usuarioLogado: string
  ): Observable<any> {

    const headers = new HttpHeaders({
      'X-Usuario-Logado': usuarioLogado
    });

    // Envia a requisição para inativar
    return this.http.patch<any>(
      `${this.apiUrl}/delete/${servicosId}`,
      {},
      { headers: headers }
    );
  }

  // Método para atualizar um serviço
  atualizarServicos(
    servicosId: string,
    descServico: string,
    precoServico: string,
    usuarioLogado: string
  ): Observable<any> {

    const dadosAtualizacao = {
      descServico: descServico,
      precoServico: precoServico,
      status: status
    };

    const headers = new HttpHeaders({
      'X-Usuario-Logado': usuarioLogado
    });

    // Envia a requisição para atualizar
    return this.http.patch<any>(
      `${this.apiUrl}/update/${servicosId}`,
      dadosAtualizacao,
      { headers: headers }
    );
  }
}