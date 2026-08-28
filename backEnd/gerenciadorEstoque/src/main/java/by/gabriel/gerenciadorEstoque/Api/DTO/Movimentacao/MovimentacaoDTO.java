package by.gabriel.gerenciadorEstoque.Api.DTO.Movimentacao;

import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.AcaoMovimentacao;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.TipoEntidade;
import java.time.LocalDateTime;

public record MovimentacaoDTO(
        Long movId,
        TipoEntidade tipoEntidade,
        String registroAfetadoId,
        String nomeRegistroAfetado,
        AcaoMovimentacao acaoMov,
        String campoAfetado,
        LocalDateTime dataMov,
        String responsavel
) {}