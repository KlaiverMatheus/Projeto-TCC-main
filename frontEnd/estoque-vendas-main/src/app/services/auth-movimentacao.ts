import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { movimentacaoDTO } from '../models/movimentacaoDTO'; 

@Injectable({
  providedIn: 'root',
})
export class AuthMovimentacao {
  private apiUrl = 'http://localhost:8080/movimentacao'; 

  constructor(private http: HttpClient) {}

  listarMovimentacoes(): Observable<movimentacaoDTO[]> { // <-- Alterado aqui
    return this.http.get<movimentacaoDTO[]>(`${this.apiUrl}/listAllMov`); // <-- Alterado aqui
  }

  filtrarMovimentacoes(filtros: any): Observable<movimentacaoDTO[]> { // <-- Alterado aqui
    let params = new HttpParams();

    if (filtros.tipo) params = params.set('tipo', filtros.tipo);
    if (filtros.dataInicio) params = params.set('dataInicio', filtros.dataInicio);
    if (filtros.dataFim) params = params.set('dataFim', filtros.dataFim);
    if (filtros.acao) params = params.set('acao', filtros.acao);
    if (filtros.registroAfetado) params = params.set('registroAfetado', filtros.registroAfetado);
    if (filtros.responsavel) params = params.set('responsavel', filtros.responsavel);

    return this.http.get<movimentacaoDTO[]>(`${this.apiUrl}/filtrar`, { params }); // <-- Alterado aqui
  }
}