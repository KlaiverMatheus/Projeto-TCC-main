
import { HttpClient, HttpHeaders } from '@angular/common/http'; //LINHA ALTERADA ATUALIZADO
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class cadastroUsuario {
  private apiUrl = 'http://localhost:8080/usuario/cadastro' //ATUALIZADO

  constructor(private http: HttpClient) {}

  cadastroUsuario(nome: string, senha: string, email: any, telefone: string, userCargo: string, usuarioLogado: string): Observable<any> { //LINHA ALTERADA
    const dadosCadastroUsuario = {
      nome: nome,
      senha: senha,
      email: email,
      telefone: telefone,
      userCargo: userCargo // ATUALIZADO
    };

  // Cria o Header exigido pelo backend ///LINHA ALTERADA METODO NOVO ATUALIZADO
      const headers = new HttpHeaders({
        'X-Usuario-Logado': usuarioLogado
      });

    return this.http.post<any>(this.apiUrl, dadosCadastroUsuario, { headers: headers }); // ATUALIZADO
  }
}
