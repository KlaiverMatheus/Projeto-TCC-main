package by.gabriel.gerenciadorEstoque.Api.Controller;

import by.gabriel.gerenciadorEstoque.Api.DTO.Response.VendaResponseDTO;
import by.gabriel.gerenciadorEstoque.Api.DTO.Venda.PagVendaDTO;
import by.gabriel.gerenciadorEstoque.Api.DTO.Venda.VendaDTO;
import by.gabriel.gerenciadorEstoque.Model.Vendas.Venda;
import by.gabriel.gerenciadorEstoque.Services.VendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venda")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping("/criar")
    public ResponseEntity<VendaResponseDTO> criarVendaAberta (@RequestBody VendaDTO dto,  @RequestHeader ("X-Usuario-Logado") String usuarioLogado) {

        Venda novaVenda = vendaService.criarVendaAberta(dto, usuarioLogado);

        VendaResponseDTO response = new VendaResponseDTO(
                novaVenda.getId(),
                novaVenda.getCliente(),
                novaVenda.getValorTotal(),
                novaVenda.getStatus(),
                "Venda aberta com sucesso!"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PostMapping("/finalizar/{id}")
    public ResponseEntity<VendaResponseDTO> finalizarVenda (@PathVariable Long id, @RequestBody List<PagVendaDTO> pagDto, @RequestHeader ("X-Usuario-Logado") String usuarioLogado) {

        Venda vendaFinalizada = vendaService.finalizarVenda(id, pagDto, usuarioLogado);

        VendaResponseDTO response = new VendaResponseDTO(
                vendaFinalizada.getId(),
                vendaFinalizada.getCliente(),
                vendaFinalizada.getValorTotal(),
                vendaFinalizada.getStatus(),
                "Venda finalizada com sucesso!"
        );

        return ResponseEntity.ok(response);

    }


}
