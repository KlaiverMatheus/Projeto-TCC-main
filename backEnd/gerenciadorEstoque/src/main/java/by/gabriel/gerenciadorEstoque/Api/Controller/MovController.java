package by.gabriel.gerenciadorEstoque.Api.Controller;

import by.gabriel.gerenciadorEstoque.Api.DTO.Movimentacao.MovimentacaoDTO;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.AcaoMovimentacao;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.TipoEntidade;
import by.gabriel.gerenciadorEstoque.Services.MovimentacaoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movimentacao")
public class MovController {

    private final MovimentacaoService movimentacaoService;

    public MovController(MovimentacaoService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    @GetMapping("/listAllMov")
    public ResponseEntity<List<MovimentacaoDTO>> listAllMov() {
        List<MovimentacaoDTO> listados = movimentacaoService.listAllMov();
        return ResponseEntity.ok(listados);
    }

    @GetMapping("/filtrar")
    public ResponseEntity<List<MovimentacaoDTO>> filtrarMovimentacoes(
            @RequestParam(required = false) TipoEntidade tipo, // <-- NOVO FILTRO GENÉRICO
            @RequestParam(required = false) AcaoMovimentacao acao,
            @RequestParam(required = false) String responsavel,
            @RequestParam(required = false) String registroAfetado, // <-- RENOMEADO
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim
    ) {
        List<MovimentacaoDTO> listados = movimentacaoService.filtrarMovimentacoes(
                tipo, acao, responsavel, registroAfetado, dataInicio, dataFim
        );
        return ResponseEntity.ok(listados);
    }
}