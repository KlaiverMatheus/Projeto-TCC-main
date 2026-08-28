export enum FormaPagStatus {
  ATIVO = 'ATIVO',
  DESATIVADO = 'DESATIVADO'
}

export interface SelectFormPag{
  descricao: string;
  status: string;
}