package by.gabriel.gerenciadorEstoque.Model.Cliente;


import by.gabriel.gerenciadorEstoque.Enum.Cliente.ClienteStatus;
import jakarta.persistence.*;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String nome;

   private String sobrenome;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String telefone;

    @Enumerated(EnumType.STRING)
    private ClienteStatus status;

    public Cliente() {

    }

    public Cliente(String nome, String sobrenome, String email, String telefone, ClienteStatus status) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.telefone = telefone;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public ClienteStatus getStatus() {
        return status;
    }

    public void setStatus(ClienteStatus status) {
        this.status = status;
    }
}
