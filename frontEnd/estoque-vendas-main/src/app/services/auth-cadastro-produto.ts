import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthCadastroProduto {
  private apiUrl = 'http://localhost:8080/produto/cadastro'

  constructor (private http: HttpClient) {}

  cadastroProduto(nome: string, marca: string, codBarra: string, quantidade: number, precoCusto: number, precoVenda: number, usuarioLogado: string): Observable<any> {
    const dadosCadastroProduto = {
      nome: nome,
      marca: marca,
      codBarra: codBarra,
      quantidade: quantidade,
      precoCusto: precoCusto,
      precoVenda: precoVenda,
      usuarioLogado: usuarioLogado
    };

      const headers = new HttpHeaders({
        'X-Usuario-Logado': usuarioLogado
      });

      return this.http.post<any>(this.apiUrl, dadosCadastroProduto, {headers: headers});
  }
}
