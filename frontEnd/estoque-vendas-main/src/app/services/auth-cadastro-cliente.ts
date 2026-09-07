import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthCadastroCliente {
  private apiUrl = 'http://localhost:8080/cliente/cadastrar'

  constructor(private http: HttpClient){}

  cadastroCliente(nome: string, sobrenome: string, email: string, telefone: string, usuarioLogado: string): Observable<any> {
    const dadosCadastroCliente = {
      nome: nome,
      sobrenome: sobrenome,
      email: email,
      telefone: telefone,
      usuarioLogado: usuarioLogado
    }

      const headers = new HttpHeaders({
        'X-Usuario-Logado': usuarioLogado
      });

      return this.http.post<any>(this.apiUrl, dadosCadastroCliente, {headers: headers});
  }
  
}
