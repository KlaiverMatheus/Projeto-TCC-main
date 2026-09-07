package by.gabriel.gerenciadorEstoque.Services;

import by.gabriel.gerenciadorEstoque.Api.DTO.Cliente.ClienteDTO;
import by.gabriel.gerenciadorEstoque.Api.DTO.Cliente.Consultas.ClienteSelectDTO;
import by.gabriel.gerenciadorEstoque.Enum.Cliente.ClienteStatus;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.AcaoMovimentacao;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.TipoEntidade;
import by.gabriel.gerenciadorEstoque.Exception.Cliente.ClienteNaoEncontrado;
import by.gabriel.gerenciadorEstoque.Exception.Cliente.EmailClienteJaExiste;
import by.gabriel.gerenciadorEstoque.Exception.Cliente.NomeClienteNotNull;
import by.gabriel.gerenciadorEstoque.Exception.Usuario.UserNotFoundException;
import by.gabriel.gerenciadorEstoque.Model.Cliente.Cliente;
import by.gabriel.gerenciadorEstoque.Model.Movimentacao.Movimentacao;
import by.gabriel.gerenciadorEstoque.Model.Usuario.Usuario;
import by.gabriel.gerenciadorEstoque.Repository.Cliente.ClienteRepository;
import by.gabriel.gerenciadorEstoque.Repository.Movimentacao.MovimentacaoRepository;
import by.gabriel.gerenciadorEstoque.Repository.Usuario.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final MovimentacaoRepository movimentacaoRepository;
    private final UserRepository userRepository;

    public ClienteService(ClienteRepository clienteRepository, MovimentacaoRepository movimentacaoRepository, UserRepository userRepository) {
        this.clienteRepository = clienteRepository;
        this.movimentacaoRepository = movimentacaoRepository;
        this.userRepository = userRepository;
    }

    // --- LISTAGEM ---
    public List<ClienteSelectDTO> listarClientesAtivos() {
        return clienteRepository.findByStatusCustom(ClienteStatus.ATIVO);
    }

    // --- CADASTRO ---
    @Transactional
    public Cliente cadastroCliente(ClienteDTO dto, String usuarioLogado) {
        Usuario userLogado = userRepository.findByNomeIgnoreCase(usuarioLogado)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado"));

        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new NomeClienteNotNull("O nome do cliente é obrigatório");
        }

        if (dto.email() != null && !dto.email().isBlank() && clienteRepository.findByEmailIgnoreCase(dto.email()).isPresent()) {
            throw new EmailClienteJaExiste("Já existe um cliente cadastrado com este e-mail");
        }

        Cliente novoCliente = new Cliente(
                dto.nome(),
                dto.sobrenome(),
                dto.email(),
                dto.telefone(),
                ClienteStatus.ATIVO
        );

        Cliente clienteSalvo = clienteRepository.save(novoCliente);

        // Movimentação com UUID nulo e ID numérico preenchido
        movimentacaoRepository.save(new Movimentacao(
                AcaoMovimentacao.CRIACAO,
                TipoEntidade.CLIENTE,
                null,
                clienteSalvo.getId(),
                clienteSalvo.getNome(),
                "NENHUM",
                userLogado
        ));

        return clienteSalvo;
    }

    // --- ATUALIZAÇÃO ---
    @Transactional
    public Cliente atualizarCliente(Long id, ClienteDTO dto, String usuarioLogado) {

        Usuario userLogado = userRepository.findByNomeIgnoreCase(usuarioLogado)
                .orElseThrow(() -> new UserNotFoundException("Usuário logado não encontrado"));

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontrado("Cliente não encontrado"));

        if (dto.nome() != null && !dto.nome().equalsIgnoreCase(cliente.getNome())) {

            if (dto.nome().isBlank()) throw new NomeClienteNotNull("Nome não pode ser vazio");

            cliente.setNome(dto.nome());
            movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.ATUALIZACAO, TipoEntidade.CLIENTE, null, cliente.getId(), cliente.getNome(), "NOME", userLogado));
        }

        if (dto.sobrenome() != null && !dto.sobrenome().equalsIgnoreCase(cliente.getSobrenome())) {
            cliente.setSobrenome(dto.sobrenome());
            movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.ATUALIZACAO, TipoEntidade.CLIENTE, null, cliente.getId(), cliente.getNome(), "SOBRENOME", userLogado));
        }

        if (dto.email() != null && !dto.email().equalsIgnoreCase(cliente.getEmail())) {
            if (clienteRepository.findByEmailIgnoreCase(dto.email()).isPresent()) {
                throw new EmailClienteJaExiste("Já existe um cliente cadastrado com este e-mail");
            }
            cliente.setEmail(dto.email());
            movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.ATUALIZACAO, TipoEntidade.CLIENTE, null, cliente.getId(), cliente.getNome(), "EMAIL", userLogado));
        }

        if (dto.telefone() != null && !dto.telefone().equalsIgnoreCase(cliente.getTelefone())) {
            cliente.setTelefone(dto.telefone());
            movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.ATUALIZACAO, TipoEntidade.CLIENTE, null, cliente.getId(), cliente.getNome(), "TELEFONE", userLogado));
        }

        return clienteRepository.save(cliente);
    }

    // --- DELEÇÃO LÓGICA ---
    @Transactional
    public boolean deletarCliente(Long id, String usuarioLogado) {
        Usuario userLogado = userRepository.findByNomeIgnoreCase(usuarioLogado)
                .orElseThrow(() -> new UserNotFoundException("Usuário logado não encontrado"));

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontrado("Cliente não encontrado"));

        cliente.setStatus(ClienteStatus.INATIVO);
        clienteRepository.save(cliente);

        movimentacaoRepository.save(new Movimentacao(
                AcaoMovimentacao.EXCLUSAO,
                TipoEntidade.CLIENTE,
                null,
                cliente.getId(),
                cliente.getNome(),
                "NENHUM",
                userLogado
        ));

        return true;
    }
}