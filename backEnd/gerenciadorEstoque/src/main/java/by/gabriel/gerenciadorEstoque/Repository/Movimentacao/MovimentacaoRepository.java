package by.gabriel.gerenciadorEstoque.Repository.Movimentacao;

import by.gabriel.gerenciadorEstoque.Api.DTO.Movimentacao.MovimentacaoDTO;
import by.gabriel.gerenciadorEstoque.Model.Movimentacao.Movimentacao;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.AcaoMovimentacao;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.TipoEntidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    @Query("""
        SELECT new by.gabriel.gerenciadorEstoque.Api.DTO.Movimentacao.MovimentacaoDTO(
            m.id,
            m.tipoEntidade,
            COALESCE(CAST(m.registroIntId AS string), 
            CAST(m.registroStringId AS string)),
            m.nomeRegistroAfetado,
            m.acao,
            m.campoAfetado,
            m.dataHora,
            m.autor.nome
        )
        FROM Movimentacao m
        ORDER BY m.dataHora DESC
        """)
    List<MovimentacaoDTO> listAllMov();

    @Query("""
        SELECT new by.gabriel.gerenciadorEstoque.Api.DTO.Movimentacao.MovimentacaoDTO(
            m.id,
            m.tipoEntidade,
            COALESCE(CAST(m.registroIntId AS string), 
            CAST(m.registroStringId AS string)),
            m.nomeRegistroAfetado,
            m.acao,
            m.campoAfetado,
            m.dataHora,
            m.autor.nome
        )
        FROM Movimentacao m
        WHERE (:tipo IS NULL OR m.tipoEntidade = :tipo)
          AND (:acao IS NULL OR m.acao = :acao)
          AND (:responsavel IS NULL OR LOWER(m.autor.nome) LIKE LOWER(CONCAT('%', :responsavel, '%')))
          AND (:registroAfetado IS NULL OR LOWER(m.nomeRegistroAfetado) LIKE LOWER(CONCAT('%', :registroAfetado, '%')))
          AND (m.dataHora BETWEEN :dataInicio AND :dataFim)
        ORDER BY m.dataHora DESC
        """)
    List<MovimentacaoDTO> filtrarMovimentacoes(
            @Param("tipo") TipoEntidade tipo,
            @Param("acao") AcaoMovimentacao acao,
            @Param("responsavel") String responsavel,
            @Param("registroAfetado") String registroAfetado,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );
}