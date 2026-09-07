package by.gabriel.gerenciadorEstoque.Services;

import by.gabriel.gerenciadorEstoque.Api.DTO.Produto.Consultas.SelectAllProdDTO;
import by.gabriel.gerenciadorEstoque.Api.DTO.Produto.ProdutoDTO;
import by.gabriel.gerenciadorEstoque.Api.DTO.Produto.UpdateProdDTO;
import by.gabriel.gerenciadorEstoque.Exception.Produto.*;
import by.gabriel.gerenciadorEstoque.Exception.Usuario.UserNotFoundException;
import by.gabriel.gerenciadorEstoque.Exception.Usuario.UserNotPermission;
import by.gabriel.gerenciadorEstoque.Model.Movimentacao.Movimentacao;
import by.gabriel.gerenciadorEstoque.Model.Produto.Produto;
import by.gabriel.gerenciadorEstoque.Model.Usuario.Usuario;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.AcaoMovimentacao;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.TipoEntidade;
import by.gabriel.gerenciadorEstoque.Enum.Produto.ProdStatus;
import by.gabriel.gerenciadorEstoque.Enum.Usuario.UserCargo;
import by.gabriel.gerenciadorEstoque.Repository.Movimentacao.MovimentacaoRepository;
import by.gabriel.gerenciadorEstoque.Repository.Produto.ProdutoRepository;
import by.gabriel.gerenciadorEstoque.Repository.Usuario.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    public List<SelectAllProdDTO> listAllProd() {
        List<SelectAllProdDTO> listados = produtoRepository.findByStatusCustom(ProdStatus.ATIVO);
        if (listados.isEmpty()) {
            throw new ListaProdVaziaException("Não possui nenhum registro no banco de dados");
        }
        return listados;
    }

    @Transactional
    public Produto cadastrarProduto(ProdutoDTO dto, String username) {
        Usuario usuario = userRepository.findByNomeIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("Usuario: " + username + " não encontrado"));

        if (usuario.getUserCargo() == null || (usuario.getUserCargo() != UserCargo.ADMINISTRADOR && usuario.getUserCargo() != UserCargo.DEV)) {
            throw new UserNotPermission("Usuario sem permissão para realizar esta ação!");
        }

        if(dto.nome() == null || dto.nome().isBlank()) throw new NomeProdVazioException("Nome obrigatório do produto não preenchido");

        if(dto.marca() == null || dto.marca().isBlank()) throw new MarcaNotNullException("Marca não pode estar vazia");

        if (produtoRepository.findByNomeIgnoreCase(dto.nome()).isPresent()){
            throw new NomeProdJaExistenteException("Produto já registrado com este nome");
        }

        if(dto.codBarra() == null || dto.codBarra().length() < 13) throw new CodBarraMenorException("Código de barras inválido menor que 13 digitos");

        if (produtoRepository.findByCodBarraIgnoreCase(dto.codBarra()).isPresent()) {
            throw new CodBarraExistenteException("Código de barras já registrado");
        }

        if(dto.quantidade() == null || dto.quantidade() <= 0) throw new QuantidadeMenorZeroException("Quantidade deve ser maior que zero");

        if(dto.precoCusto() == null || dto.precoVenda() == null) {
            throw new PrecosNotNullException("Preços não podem ser nulos");
        }

        Produto produto = new Produto(
                dto.nome(),
                dto.marca(),
                dto.codBarra(),
                dto.quantidade(),
                dto.precoCusto(),
                dto.precoVenda(),
                ProdStatus.ATIVO
        );
        produto = produtoRepository.save(produto);

        // Movimentação Genérica COM O NOME DO PRODUTO AGORA!
        movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.CRIACAO, TipoEntidade.PRODUTO, null, produto.getProdId(), produto.getNome(), "NENHUM", usuario));

        return produto;
    }

    @Transactional
    public Produto updateProd(Long id, UpdateProdDTO dto, String userName) {

        Usuario usuario = userRepository.findByNomeIgnoreCase(userName)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));

        if (!(usuario.getUserCargo().toString().equals("ADMINISTRADOR") || usuario.getUserCargo().toString().equals("DEV"))) {
            throw new AccessDeniedException("Apenas Usuários Administradores podem realizar esta ação");
        }

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ProdNotFoundException("Id " + id + " é inválido, produto não encontrado"));

        String idProd = String.valueOf(produto.getProdId());

        if (dto.nome() != null && !dto.nome().equalsIgnoreCase(produto.getNome())) {
            if (dto.nome().isBlank()) throw new NomeProdVazioException("Nome do produto não pode ser vazio");
            produto.setNome(dto.nome().toLowerCase());
            movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.ATUALIZACAO, TipoEntidade.PRODUTO, null, produto.getProdId(), produto.getNome(), "NOME", usuario));
        }

        if (dto.marca() != null && !dto.marca().equalsIgnoreCase(produto.getMarca())) {
            if (dto.marca().isBlank()) throw new MarcaNotNullException("Marca não pode estar vazia");
            produto.setMarca(dto.marca().toLowerCase());
            movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.ATUALIZACAO, TipoEntidade.PRODUTO, null, produto.getProdId(), produto.getNome(), "MARCA", usuario));
        }

        if (dto.codBarra() != null && !dto.codBarra().equalsIgnoreCase(produto.getCodBarra())) {
            if (dto.codBarra().isBlank()) throw new CodBarraVazioException("Codigo de barras não pode estar vazio");
            if (dto.codBarra().length() < 13) throw new CodBarraMenorException("Código de barras deve conter ao menos 13 dígitos");
            produto.setCodBarra(dto.codBarra());
            movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.ATUALIZACAO, TipoEntidade.PRODUTO, null, produto.getProdId(), produto.getNome(), "CODBARRA", usuario));
        }

        if (dto.quantidade() != null && !dto.quantidade().equals(produto.getQuantidade())) {
            if (dto.quantidade() <= 0) throw new QuantidadeMenorZeroException("Quantidade não pode ser menor ou igual a zero");
            produto.setQuantidade(dto.quantidade());
            movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.ATUALIZACAO, TipoEntidade.PRODUTO, null, produto.getProdId(), produto.getNome(), "QUANTIDADE", usuario));
        }

        if (dto.precoCusto() != null && dto.precoCusto().compareTo(produto.getPrecoCusto()) != 0) {
            if (dto.precoCusto().compareTo(BigDecimal.ZERO) <= 0 || dto.precoCusto().compareTo(produto.getPrecoVenda()) > 0) {
                throw new CostOrSellBellowZeroException("Preço de Custo inválido");
            }
            produto.setPrecoCusto(dto.precoCusto());
            movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.ATUALIZACAO, TipoEntidade.PRODUTO, null, produto.getProdId(), produto.getNome(), "PRECOCUSTO", usuario));
        }

        if (dto.precoVenda() != null && dto.precoVenda().compareTo(produto.getPrecoVenda()) != 0) {
            if (dto.precoVenda().compareTo(BigDecimal.ZERO) <= 0 || dto.precoVenda().compareTo(produto.getPrecoCusto()) < 0) {
                throw new CostOrSellBellowZeroException("Preço de Venda inválido");
            }
            produto.setPrecoVenda(dto.precoVenda());
            movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.ATUALIZACAO, TipoEntidade.PRODUTO, null, produto.getProdId(), produto.getNome(), "PRECOVENDA", usuario));
        }

        return produtoRepository.save(produto);
    }

    @Transactional
    public boolean deletarProd(Long id, String userName) {
        Usuario usuario = userRepository.findByNomeIgnoreCase(userName).orElseThrow(() -> new UserNotFoundException("Usuario não encontrado"));
        UserCargo cargo = usuario.getUserCargo();

        if(cargo == null || (cargo != UserCargo.ADMINISTRADOR && cargo != UserCargo.DEV)) {
            throw new UserNotPermission("Usuario sem permissão");
        }

        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ProdNotFoundException("Produto Não foi encontrado"));

        produto.setProdStatus(ProdStatus.INATIVO);
        produtoRepository.save(produto);

        movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.EXCLUSAO, TipoEntidade.PRODUTO, null, produto.getProdId(), produto.getNome(), "NENHUM", usuario));

        return true;
    }
}