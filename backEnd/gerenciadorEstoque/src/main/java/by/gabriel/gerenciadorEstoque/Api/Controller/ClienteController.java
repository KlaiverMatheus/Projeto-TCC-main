package by.gabriel.gerenciadorEstoque.Api.Controller;


import by.gabriel.gerenciadorEstoque.Api.DTO.Cliente.ClienteDTO;
import by.gabriel.gerenciadorEstoque.Api.DTO.Cliente.Consultas.ClienteSelectDTO;
import by.gabriel.gerenciadorEstoque.Model.Cliente.Cliente;
import by.gabriel.gerenciadorEstoque.Services.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/cliente")
@CrossOrigin(origins = "*") // Ajuste a origem conforme a configuração do seu frontend Angular
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // --- LISTAGEM DE CLIENTES ATIVOS ---
    @GetMapping("/listAll")
    public ResponseEntity<List<ClienteSelectDTO>> listarClientes() {
        List<ClienteSelectDTO> clientes = clienteService.listarClientesAtivos();
        return ResponseEntity.ok(clientes);
    }

    // --- CADASTRO DE CLIENTE ---
    @PostMapping("/cadastrar")
    public ResponseEntity<Cliente> cadastrarCliente(@RequestBody ClienteDTO dto, Principal principal) {
        // principal.getName() extrai o nome (ou e-mail) do usuário logado via Spring Security/JWT
        String usuarioLogado = principal.getName();

        Cliente novoCliente = clienteService.cadastroCliente(dto, usuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    // --- ATUALIZAÇÃO DE CLIENTE ---
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Cliente> atualizarCliente(@PathVariable Long id, @RequestBody ClienteDTO dto, Principal principal) {
        String usuarioLogado = principal.getName();

        Cliente clienteAtualizado = clienteService.atualizarCliente(id, dto, usuarioLogado);
        return ResponseEntity.ok(clienteAtualizado);
    }

    // --- DELEÇÃO LÓGICA DE CLIENTE ---
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarCliente(@PathVariable Long id, Principal principal) {
        String usuarioLogado = principal.getName();

        clienteService.deletarCliente(id, usuarioLogado);
        return ResponseEntity.ok("Cliente inativado com sucesso!");
    }
}
