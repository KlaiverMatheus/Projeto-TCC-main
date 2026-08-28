package by.gabriel.gerenciadorEstoque.Services;

import by.gabriel.gerenciadorEstoque.Api.DTO.Movimentacao.MovimentacaoDTO;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.AcaoMovimentacao;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.TipoEntidade;
import by.gabriel.gerenciadorEstoque.Repository.Movimentacao.MovimentacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository){
        this.movimentacaoRepository = movimentacaoRepository;
    }

    public List<MovimentacaoDTO> listAllMov() {
        List<MovimentacaoDTO> movimentacoes = movimentacaoRepository.listAllMov();

        if(movimentacoes.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma movimentação registrada");
        }

        return movimentacoes;
    }

    public List<MovimentacaoDTO> filtrarMovimentacoes(
            TipoEntidade tipo,      // <-- Adicionado para você poder filtrar a tabela no Angular!
            AcaoMovimentacao acao,
            String responsavel,
            String registroAfetado, // <-- Renomeado de usuarioAfetado para servir pra Produto e Cliente também
            LocalDate dataInicioFiltro,
            LocalDate dataFimFiltro) {

        // Mantemos a sua lógica impecável do operador ternário para as datas nulas
        LocalDateTime inicioBusca = (dataInicioFiltro != null)
                ? dataInicioFiltro.atStartOfDay()
                : LocalDateTime.of(2000, 1, 1, 0, 0);

        LocalDateTime fimBusca = (dataFimFiltro != null)
                ? dataFimFiltro.atTime(23, 59, 59)
                : LocalDateTime.of(2100, 12, 31, 23, 59, 59);

        // Repassando todos os filtros, incluindo o 'tipo', para a consulta JPQL nova
        List<MovimentacaoDTO> movimentacoes = movimentacaoRepository.filtrarMovimentacoes(
                tipo, acao, responsavel, registroAfetado, inicioBusca, fimBusca
        );

        if(movimentacoes.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma movimentação encontrada para os filtros informados");
        }

        return movimentacoes;
    }
}