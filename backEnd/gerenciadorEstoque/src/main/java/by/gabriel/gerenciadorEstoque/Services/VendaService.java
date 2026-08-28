package by.gabriel.gerenciadorEstoque.Services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import by.gabriel.gerenciadorEstoque.Api.DTO.Venda.PagVendaDTO;
import by.gabriel.gerenciadorEstoque.Api.DTO.Venda.VendaDTO;
import by.gabriel.gerenciadorEstoque.Exception.Usuario.UserNotFoundException;
import by.gabriel.gerenciadorEstoque.Model.FormaPag.FormaPagto;
import by.gabriel.gerenciadorEstoque.Model.Produto.Produto;
import by.gabriel.gerenciadorEstoque.Model.Usuario.Usuario;
import by.gabriel.gerenciadorEstoque.Model.Vendas.ItensVenda;
import by.gabriel.gerenciadorEstoque.Model.Vendas.PagVenda;
import by.gabriel.gerenciadorEstoque.Model.Vendas.Venda;
import by.gabriel.gerenciadorEstoque.Enum.Venda.VendaStatus;
import by.gabriel.gerenciadorEstoque.Repository.FormPagRespository.FormPagRepository;
import by.gabriel.gerenciadorEstoque.Repository.Produto.ProdutoRepository;
import by.gabriel.gerenciadorEstoque.Repository.Usuario.UserRepository;
import by.gabriel.gerenciadorEstoque.Repository.Vendas.VendaRepository;
import jakarta.transaction.Transactional;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final FormPagRepository formaPagtoRepository;
    private final UserRepository userRepository;
    private final ProdutoRepository produtoRepository;

    public VendaService(VendaRepository vendaRepository, ProdutoRepository produtoRepository, UserRepository userRepository, FormPagRepository formaPagtoRepository) {
        this.vendaRepository = vendaRepository;
        this.userRepository = userRepository;
        this.produtoRepository = produtoRepository;
        this.formaPagtoRepository = formaPagtoRepository;
    }

    @Transactional
    public Venda criarVendaAberta(VendaDTO vDto, String usuarioLogado) {

        Usuario userLogado = userRepository.findByNomeIgnoreCase(usuarioLogado)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        Venda venda = new Venda();
        venda.setUsuario(userLogado);
        venda.setCliente(vDto.cliente());
        venda.setDataVenda(LocalDateTime.now());
        venda.setStatus(VendaStatus.ABERTA);
        venda.setDesconto(vDto.desconto() != null ? vDto.desconto() : BigDecimal.ZERO);

        BigDecimal valorTotalCalculado = BigDecimal.ZERO;
        List<ItensVenda> listaItens = new ArrayList<>();

        // Apenas calcula o valor e monta os itens, SEM DAR BAIXA NO ESTOQUE AINDA
        for (var itemDto : vDto.itensVenda()) {

            Produto produto = produtoRepository.findById(itemDto.produtoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            ItensVenda itemVenda = new ItensVenda();
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

        // Salva a venda como aberta (opcionalmente pode salvar pagamentos parciais se houver)
        return vendaRepository.save(venda);
    }

    @Transactional
    public Venda finalizarVenda(Long vendaId, List<PagVendaDTO> pagamentosDto, String usuarioLogado) {

        Usuario userLogado = userRepository.findByNomeIgnoreCase(usuarioLogado)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        Venda venda = vendaRepository.findById(vendaId)
                .orElseThrow(() -> new RuntimeException("Venda não encontrada"));

        if (venda.getStatus() != VendaStatus.ABERTA) {
            throw new RuntimeException("Esta venda não está aberta para finalização.");
        }

        for (var itemVenda : venda.getItensVenda()) {

            Produto produto = itemVenda.getProduto();

            if (produto.getQuantidade() < itemVenda.getQuantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            produto.setQuantidade(produto.getQuantidade() - itemVenda.getQuantidade());
            produtoRepository.save(produto);
        }

        // Processa as formas de pagamento
        if (venda.getPagVenda() == null) {
            venda.setPagVenda(new ArrayList<>());
        } else {
            venda.getPagVenda().clear();
        }

        List<PagVenda> listaPagamentos = venda.getPagVenda();
        BigDecimal totalPago = BigDecimal.ZERO;

        for (var pagDto : pagamentosDto) {
            FormaPagto formapagto = formaPagtoRepository.findById(pagDto.formaPagId())
                    .orElseThrow(() -> new RuntimeException("Forma de pagamento não encontrada"));

            PagVenda pagVenda = new PagVenda();
            pagVenda.setVenda(venda);
            pagVenda.setFormaPagto(formapagto);
            pagVenda.setValorPago(pagDto.valorPago());

            listaPagamentos.add(pagVenda);
            totalPago = totalPago.add(pagDto.valorPago());
        }

        // Valida se o dinheiro pago cobre o total da venda
        if (totalPago.compareTo(venda.getValorTotal()) < 0) {
            throw new RuntimeException("O valor total pago é menor que o valor total da venda.");
        }

        venda.setStatus(VendaStatus.FINALIZADA);

        return vendaRepository.save(venda);
    }
}