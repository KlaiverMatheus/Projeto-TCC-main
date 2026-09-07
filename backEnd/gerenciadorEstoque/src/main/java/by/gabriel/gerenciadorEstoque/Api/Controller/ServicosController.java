package by.gabriel.gerenciadorEstoque.Api.Controller;

import by.gabriel.gerenciadorEstoque.Api.DTO.Servicos.ServicosDTO;
import by.gabriel.gerenciadorEstoque.Api.DTO.Servicos.Consultas.ServicosSelectDTO;
import by.gabriel.gerenciadorEstoque.Model.Servicos.Servicos;
import by.gabriel.gerenciadorEstoque.Services.ServicosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicosController {

    private final ServicosService servicosService;

    public ServicosController(ServicosService servicosService) {
        this.servicosService = servicosService;
    }

    // --- LISTAGEM DE SERVIÇOS ---
    @GetMapping("/listAll")
    public ResponseEntity<List<ServicosSelectDTO>> listarServicos() {
        List<ServicosSelectDTO> servicos = servicosService.listarServicosAtivos();
        return ResponseEntity.ok(servicos);
    }

    // --- CADASTRO ---
    @PostMapping("/cadastrar")
    public ResponseEntity<Servicos> cadastrarServico(
            @RequestBody ServicosDTO dto,
            @RequestHeader("X-Usuario-Logado") String usuarioLogado) {

        Servicos novoServico = servicosService.cadastrarServico(dto, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoServico);
    }

    // --- ATUALIZAÇÃO ---
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Servicos> atualizarServico(
            @PathVariable Long id,
            @RequestBody ServicosDTO dto,
            @RequestHeader("X-Usuario-Logado") String usuarioLogado) {

        Servicos servicoAtualizado = servicosService.atualizarServico(id, dto, usuarioLogado);
        return ResponseEntity.ok(servicoAtualizado);
    }

    // --- DELEÇÃO LÓGICA ---
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarServico(
            @PathVariable Long id,
            @RequestHeader("X-Usuario-Logado") String usuarioLogado) {

        servicosService.deletarServico(id, usuarioLogado);
        return ResponseEntity.ok("Serviço inativado com sucesso!");
    }
}