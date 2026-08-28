import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { movimentacaoDTO } from '../../models/movimentacaoDTO'; 
import { AuthMovimentacao } from '../../services/auth-movimentacao';

@Component({
  selector: 'app-movimentacao',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './movimentacao.html',
  styleUrl: './movimentacao.css',
})
export class Movimentacao implements OnInit {
  carregando: boolean = false;
  buscaRealizada: boolean = false;

  listaMovimentacoes: movimentacaoDTO[] = [];
  movimentacoesExibidas: movimentacaoDTO[] = [];

  dataInicio: string = '';       
  dataFim: string = ''; 
  filtroTipo: string = '';              
  filtroAcao: string = '';       
  filtroRegistro: string = '';        
  filtroResponsavel: string = ''; 

  // NOVA VARIÁVEL: Guarda o módulo aplicado apenas após clicar em "Buscar"
  // É ela que o HTML vai olhar para esconder a coluna ID Registro
  filtroAplicadoTipo: string = ''; 

  constructor(
    private authMovimentacaoService: AuthMovimentacao,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {}

  limparFiltro(): void {
    this.dataInicio = '';
    this.dataFim = '';
    this.filtroTipo = '';         
    this.filtroAcao = '';
    this.filtroRegistro = '';     
    this.filtroResponsavel = '';
    
    // Limpa a variável visual ao limpar a busca
    this.filtroAplicadoTipo = ''; 

    this.buscaRealizada = false;
    this.listaMovimentacoes = [];
    this.movimentacoesExibidas = [];
  }

  buscarMovimentacoes(): void {
    this.carregando = true;

    // A MÁGICA AQUI: Salva o estado do select apenas no momento da busca!
    this.filtroAplicadoTipo = this.filtroTipo;

    const filtros: any = {};
    if (this.filtroTipo) filtros.tipo = this.filtroTipo;                    
    if (this.dataInicio) filtros.dataInicio = this.dataInicio;
    if (this.dataFim) filtros.dataFim = this.dataFim;
    if (this.filtroAcao) filtros.acao = this.filtroAcao;
    if (this.filtroRegistro) filtros.registroAfetado = this.filtroRegistro; 
    if (this.filtroResponsavel) filtros.responsavel = this.filtroResponsavel;

    this.authMovimentacaoService.filtrarMovimentacoes(filtros).subscribe({
      next: (dados) => {
        this.listaMovimentacoes = dados;
        this.movimentacoesExibidas = dados; 
        this.buscaRealizada = true;
        this.carregando = false;
        this.cdr.detectChanges();
      },
      error: (erro) => {
        console.error('Erro ao buscar movimentações:', erro);
        this.movimentacoesExibidas = [];
        this.buscaRealizada = true;
        this.carregando = false;
        this.cdr.detectChanges();
      }
    });
  }
}