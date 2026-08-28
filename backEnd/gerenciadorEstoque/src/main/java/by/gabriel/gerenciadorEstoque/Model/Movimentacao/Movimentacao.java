package by.gabriel.gerenciadorEstoque.Model.Movimentacao;

import by.gabriel.gerenciadorEstoque.Model.Usuario.Usuario;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.AcaoMovimentacao;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.TipoEntidade;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataHora;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @Enumerated(EnumType.STRING)
    private AcaoMovimentacao acao;

    @Enumerated(EnumType.STRING)
    private TipoEntidade tipoEntidade;

    private Long registroIntId;

    @Column(nullable = true)
    private UUID registroStringId;

    @Column(nullable = true)
    private String nomeRegistroAfetado;

    private String campoAfetado;

    public Movimentacao() {}

    // Construtor atualizado
    public Movimentacao(AcaoMovimentacao acao, TipoEntidade tipoEntidade, UUID registroStringId, Long registroIntId, String nomeRegistroAfetado, String campoAfetado, Usuario autor) {
        this.acao = acao;
        this.tipoEntidade = tipoEntidade;
        this.registroStringId = registroStringId;
        this.registroIntId = registroIntId;
        this.nomeRegistroAfetado = nomeRegistroAfetado; // <--- Não esqueça do this!
        this.campoAfetado = campoAfetado;
        this.autor = autor;
    }

    @PrePersist
    public void prePersist() {
        this.dataHora = LocalDateTime.now();
    }

    // --- GETTERS E SETTERS ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }

    public AcaoMovimentacao getAcao() { return acao; }
    public void setAcao(AcaoMovimentacao acao) { this.acao = acao; }

    public TipoEntidade getTipoEntidade() { return tipoEntidade; }
    public void setTipoEntidade(TipoEntidade tipoEntidade) { this.tipoEntidade = tipoEntidade; }

    public Long getRegistroIntId() {
        return registroIntId;
    }

    public void setRegistroIntId(Long registroIntId) {
        this.registroIntId = registroIntId;
    }

    public UUID getRegistroStringId() {
        return registroStringId;
    }

    public void setRegistroStringId(UUID registroStringId) {
        this.registroStringId = registroStringId;
    }

    public String getNomeRegistroAfetado() { return nomeRegistroAfetado; }
    public void setNomeRegistroAfetado(String nomeRegistroAfetado) { this.nomeRegistroAfetado = nomeRegistroAfetado; }

    public String getCampoAfetado() { return campoAfetado; }
    public void setCampoAfetado(String campoAfetado) { this.campoAfetado = campoAfetado; }
}