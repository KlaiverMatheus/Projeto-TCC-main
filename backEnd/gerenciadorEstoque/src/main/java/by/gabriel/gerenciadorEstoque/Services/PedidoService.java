package by.gabriel.gerenciadorEstoque.Services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import by.gabriel.gerenciadorEstoque.Api.DTO.Pedido.Consultas.PedidoListDTO;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.AcaoMovimentacao;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.TipoEntidade;
import by.gabriel.gerenciadorEstoque.Model.Cliente.Cliente;
import by.gabriel.gerenciadorEstoque.Model.Movimentacao.Movimentacao;
import by.gabriel.gerenciadorEstoque.Repository.Cliente.ClienteRepository;
import by.gabriel.gerenciadorEstoque.Repository.Movimentacao.MovimentacaoRepository;
import org.springframework.stereotype.Service;

import by.gabriel.gerenciadorEstoque.Api.DTO.Pedido.PagPedidoDTO;
import by.gabriel.gerenciadorEstoque.Api.DTO.Pedido.PedidoDTO;
import by.gabriel.gerenciadorEstoque.Exception.Usuario.UserNotFoundException;
import by.gabriel.gerenciadorEstoque.Model.FormaPag.FormaPagto;
import by.gabriel.gerenciadorEstoque.Model.Produto.Produto;
import by.gabriel.gerenciadorEstoque.Model.Usuario.Usuario;
import by.gabriel.gerenciadorEstoque.Model.Pedido.ItensPedido;
import by.gabriel.gerenciadorEstoque.Model.Pedido.PagPedido;
import by.gabriel.gerenciadorEstoque.Model.Pedido.Pedido;
import by.gabriel.gerenciadorEstoque.Enum.Pedido.PedidoStatus;
import by.gabriel.gerenciadorEstoque.Repository.FormPagRespository.FormPagRepository;
import by.gabriel.gerenciadorEstoque.Repository.Produto.ProdutoRepository;
import by.gabriel.gerenciadorEstoque.Repository.Usuario.UserRepository;
import by.gabriel.gerenciadorEstoque.Repository.Pedido.PedidoRepository;
import jakarta.transaction.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final FormPagRepository formaPagtoRepository;
    private final UserRepository userRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository; // <-- NOVO

    public PedidoService(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository,
                         UserRepository userRepository, FormPagRepository formaPagtoRepository,
                         ClienteRepository clienteRepository, MovimentacaoRepository movimentacaoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.userRepository = userRepository;
        this.produtoRepository = produtoRepository;
        this.formaPagtoRepository = formaPagtoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public Pedido criarVendaAberta(PedidoDTO vDto, String usuarioLogado) {

        Usuario userLogado = userRepository.findByNomeIgnoreCase(usuarioLogado)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + usuarioLogado));

        // Busca o cliente pelo ID vindo do DTO
        Cliente cliente = clienteRepository.findById(vDto.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o ID: " + vDto.clienteId()));

        Pedido venda = new Pedido();
        venda.setUsuario(userLogado);
        venda.setCliente(cliente); // <-- Associa o objeto Cliente
        venda.setDataVenda(LocalDateTime.now());
        venda.setStatus(PedidoStatus.ABERTA);
        venda.setDesconto(vDto.desconto() != null ? vDto.desconto() : BigDecimal.ZERO);

        BigDecimal valorTotalCalculado = BigDecimal.ZERO;
        List<ItensPedido> listaItens = new ArrayList<>();

        for (var itemDto : vDto.itensVenda()) {
            Produto produto = produtoRepository.findById(itemDto.produtoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            ItensPedido itemVenda = new ItensPedido();
            itemVenda.setVenda(venda);
            itemVenda.setProduto(produto);
            itemVenda.setQuantidade(itemDto.quantidade());
            itemVenda.setPrecoUnitario(produto.getPrecoCusto());
            itemVenda.setPrecoVenda(produto.getPrecoVenda());

            listaItens.add(itemVenda);

            BigDecimal subtotal = produto.getPrecoVenda().multiply(BigDecimal.valueOf(itemDto.quantidade()));
            valorTotalCalculado = valorTotalCalculado.add(subtotal);
        }

        valorTotalCalculado = valorTotalCalculado.subtract(venda.getDesconto());
        venda.setValorTotal(valorTotalCalculado);
        venda.setItensVenda(listaItens);

        Pedido vendaSalva = pedidoRepository.save(venda);

        return vendaSalva;
    }

    @Transactional
    public Pedido finalizarVenda(Long vendaId, List<PagPedidoDTO> pagamentosDto, String usuarioLogado) {

        Usuario userLogado = userRepository.findByNomeIgnoreCase(usuarioLogado)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + usuarioLogado));

        Pedido venda = pedidoRepository.findById(vendaId)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        if (venda.getStatus() != PedidoStatus.ABERTA) {
            throw new RuntimeException("Esta venda não está aberta para finalização.");
        }

        // Subtrai do Estoque
        for (var itemVenda : venda.getItensVenda()) {
            Produto produto = itemVenda.getProduto();

            if (produto.getQuantidade() < itemVenda.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            produto.setQuantidade(produto.getQuantidade() - itemVenda.getQuantidade());
            produtoRepository.save(produto);
        }

        if (venda.getPagVenda() == null) {
            venda.setPagVenda(new ArrayList<>());
        } else {
            venda.getPagVenda().clear();
        }

        List<PagPedido> listaPagamentos = venda.getPagVenda();
        BigDecimal totalPago = BigDecimal.ZERO;

        for (var pagDto : pagamentosDto) {
            FormaPagto formapagto = formaPagtoRepository.findById(pagDto.formaPagId())
                    .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada"));

            PagPedido pagVenda = new PagPedido();
            pagVenda.setVenda(venda);
            pagVenda.setFormaPagto(formapagto);
            pagVenda.setValorPago(pagDto.valorPago());

            listaPagamentos.add(pagVenda);
            totalPago = totalPago.add(pagDto.valorPago());
        }

        if (totalPago.compareTo(venda.getValorTotal()) < 0) {
            throw new RuntimeException("O valor total pago é menor que o valor total da venda.");
        }

        venda.setStatus(PedidoStatus.FINALIZADA);
        Pedido vendaFinalizada = pedidoRepository.save(venda);


        return vendaFinalizada;
    }

    // --- 1. ATUALIZAR VENDA ABERTA ---
    @Transactional
    public Pedido atualizarVendaAberta(Long vendaId, PedidoDTO vDto, String usuarioLogado) {

        Usuario userLogado = userRepository.findByNomeIgnoreCase(usuarioLogado)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado: " + usuarioLogado));

        Pedido pedido = pedidoRepository.findById(vendaId)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        if (pedido.getStatus() != PedidoStatus.ABERTA) {
            throw new RuntimeException("Apenas vendas ABERTAS podem ser alteradas.");
        }

        Cliente cliente = clienteRepository.findById(vDto.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com o ID: " + vDto.clienteId()));

        pedido.setCliente(cliente);
        pedido.setDesconto(vDto.desconto() != null ? vDto.desconto() : BigDecimal.ZERO);

        // Limpa os itens antigos (o orphanRemoval=true no Pedido vai deletar do banco)
        pedido.getItensVenda().clear();

        BigDecimal valorTotalCalculado = BigDecimal.ZERO;

        // Verifica se a lista não é nula antes de tentar percorrê-la
        if (vDto.itensVenda() != null) {
            for (var itemDto : vDto.itensVenda()) {
                Produto produto = produtoRepository.findById(itemDto.produtoId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

                ItensPedido itemVenda = new ItensPedido();
                itemVenda.setVenda(pedido);
                itemVenda.setProduto(produto);
                itemVenda.setQuantidade(itemDto.quantidade());
                itemVenda.setPrecoUnitario(produto.getPrecoCusto());
                itemVenda.setPrecoVenda(produto.getPrecoVenda());

                // No criar usa listaItens.add(), no atualizar usa venda.getItensVenda().add()
                pedido.getItensVenda().add(itemVenda);

                BigDecimal subtotal = produto.getPrecoVenda().multiply(BigDecimal.valueOf(itemDto.quantidade()));
                valorTotalCalculado = valorTotalCalculado.add(subtotal);
            }
        }

        valorTotalCalculado = valorTotalCalculado.subtract(pedido.getDesconto());
        pedido.setValorTotal(valorTotalCalculado);

        return pedidoRepository.save(pedido);
    }

    // --- 2. CANCELAR/EXCLUIR VENDA ABERTA (Desistência) ---
    @Transactional
    public void cancelarPedidoAberto(Long vendaId, String usuarioLogado) {

        // Valida usuário apenas para fins de auditoria/segurança da requisição
        userRepository.findByNomeIgnoreCase(usuarioLogado)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        Pedido pedido = pedidoRepository.findById(vendaId)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        if (pedido.getStatus() != PedidoStatus.ABERTA) {
            throw new RuntimeException("Apenas vendas ABERTAS podem ser canceladas por desistência.");
        }

        // Cancelamento lógico. Como não deu baixa no estoque ainda, é só mudar o status!
        pedido.setStatus(PedidoStatus.CANCELADA);
        pedidoRepository.save(pedido);
    }

    // --- 3. DEVOLUÇÃO DE VENDA FINALIZADA (Estorno) ---
    @Transactional
    public Pedido devolverPedidoinalizada(Long vendaId, String usuarioLogado) {

        userRepository.findByNomeIgnoreCase(usuarioLogado)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        Pedido pedido = pedidoRepository.findById(vendaId)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        if (pedido.getStatus() != PedidoStatus.FINALIZADA) {
            throw new RuntimeException("Apenas vendas FINALIZADAS podem ser devolvidas.");
        }

        // Devolve os itens para o estoque do Produto
        for (ItensPedido item : pedido.getItensVenda()) {
            Produto produto = item.getProduto();
            produto.setQuantidade(produto.getQuantidade() + item.getQuantidade());
            produtoRepository.save(produto);
        }

        // Estorna o status da venda para manter o histórico, mas inutilizar os totais
        pedido.setStatus(PedidoStatus.DEVOLVIDA);

        return pedidoRepository.save(pedido);
    }

    // --- 6. LISTAR TODAS AS VENDAS (Resumo para a Tabela) ---
    public List<PedidoListDTO> listarTodasAsVendas() {

        List<Pedido> vendas = pedidoRepository.findAll();

        return vendas.stream().map(venda -> new PedidoListDTO(
                venda.getId(),
                venda.getCliente().getNome(),
                venda.getValorTotal(),
                venda.getStatus(),
                venda.getDataVenda()
        )).toList();
    }

    // --- 7. BUSCAR VENDA POR ID (Detalhes completos para Editar) ---
    public Pedido buscarVendaPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada com o ID: " + id));
    }
}