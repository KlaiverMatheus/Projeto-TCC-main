export interface movimentacaoDTO { // <-- Só colocar o M maiúsculo aqui
  movId: number;
  tipoEntidade: string;         
  registroAfetadoId: string;
  nomeRegistroAfetado: string;  
  acaoMov: string;
  campoAfetado: string;
  dataMov: string;
  responsavel: string;
}