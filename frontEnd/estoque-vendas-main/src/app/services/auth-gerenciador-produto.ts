import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SelectAllProdDTO } from '../models/select-all-prod-dto';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthGerenciadorProduto {
  private apiUrl = 'http://localhost:8080/produto'

  constructor(private http: HttpClient) {}

    // metodo para buscar a lista de usuarios para a tabela
    listarProdutos(): Observable<SelectAllProdDTO[]> {
      return this.http.get<SelectAllProdDTO[]>(`${this.apiUrl}/listProd`);
    }

    // metodo para inatviar
    inativarProduto(prodId: string, usuarioLogado: string): Observable<any> {
    const headers = new HttpHeaders({
      'X-Usuario-Logado': usuarioLogado
    });
    // Envia a requisiçao para inativar
    return this.http.patch<any>(`${this.apiUrl}/delete/${prodId}`, {}, { headers: headers });
    }

    atualizarProduto(prodId: string, nome: string, marca: string, codBarra: string, quantidade: number,precoCusto: number, precoVenda: number, usuarioLogado: string): Observable<any> {
      const dadosAtualizarProduto = {
        nome: nome,
        marca: marca,
        codBarra: codBarra,
        quantidade: quantidade,
        precoCusto: precoCusto,
        precoVenda: precoVenda
      };

      const headers = new HttpHeaders({
        'X-Usuario-Logado': usuarioLogado
      });

      return this.http.patch<any>(`${this.apiUrl}/update/${prodId}`, dadosAtualizarProduto, { headers: headers });
    }
}
