package by.gabriel.gerenciadorEstoque.Services;

import java.util.List;
import java.util.UUID;

import by.gabriel.gerenciadorEstoque.Api.DTO.Usuario.Consultas.UserSelectDTO;
import by.gabriel.gerenciadorEstoque.Exception.Usuario.*;
import org.springframework.stereotype.Service;

import by.gabriel.gerenciadorEstoque.Api.DTO.Usuario.UpdateUserDTO;
import by.gabriel.gerenciadorEstoque.Api.DTO.Usuario.UserDTO;
import by.gabriel.gerenciadorEstoque.Model.Movimentacao.Movimentacao;
import by.gabriel.gerenciadorEstoque.Model.Usuario.Usuario;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.AcaoMovimentacao;
import by.gabriel.gerenciadorEstoque.Enum.Movimentacao.TipoEntidade;
import by.gabriel.gerenciadorEstoque.Enum.Usuario.UserCargo;
import by.gabriel.gerenciadorEstoque.Enum.Usuario.UserStatus;
import by.gabriel.gerenciadorEstoque.Repository.Movimentacao.MovimentacaoRepository;
import by.gabriel.gerenciadorEstoque.Repository.Usuario.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public UserService(UserRepository userRepository, MovimentacaoRepository movimentacaoRepository) {
        this.userRepository = userRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    public List<UserSelectDTO> listAllUsers(){

        List<UserSelectDTO> usuarios = userRepository.findByStatusCustom(UserStatus.ATIVO);

        if(usuarios.isEmpty()) {
            throw new NoUsersFoundInList("Não possui nenhum usuario cadastrado no sistema");
        }

        return usuarios;
    }

    public boolean logarUsuario(UserDTO dto) {

        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new UserNameNotNullException("Nome é obrigatório");
        }

        if (dto.senha() == null || dto.senha().isBlank()) {
            throw new UserPasswordNotNullException("Senha é obrigatoria");
        }

        Usuario usuario = userRepository.findByNomeIgnoreCase(dto.nome())
                .orElseThrow(() -> new UserNotFoundException("Usuario ou senha invalidos"));

        if (usuario.getUserStatus() == UserStatus.INATIVO) {
            throw new UserInactiveException("Usuario está com status inativo. Login bloqueado");
        }

        if (!usuario.validarSenha(dto.senha())) {
            throw new InvalidPasswordException("Usuario ou senha invalidos");
        }

        System.out.println("Usuario Logado com sucesso!!");

        return true;
    }

    @Transactional
    public Usuario cadastroUser(UserDTO dto, String usuarioLogado) {

        Usuario userLogado = userRepository.findByNomeIgnoreCase(usuarioLogado).orElseThrow(() ->
                new UserNotFoundException("Usuuario: " + usuarioLogado + " não foi encontrado"));

        UserCargo cargo = userLogado.getUserCargo();
        if (cargo == null || (cargo != UserCargo.ADMINISTRADOR && cargo != UserCargo.DEV)) {
            throw  new UserNotPermission("Usuario sem permissão");
        }

        if (userRepository.findByNomeIgnoreCase(dto.nome()).isPresent()) {
            throw new UserAlreadyExistsException("Usuario já existe");
        }

        if (dto.email() != null && !dto.email().isBlank() && userRepository.findByEmailIgnoreCase(dto.email()).isPresent()) {
            throw new EmailAlreadyExistException("Já existe um usuário com este email");
        }

        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new UserNameNotNullException("Nome é obrigatorio");
        }

        if (dto.senha() == null || dto.senha().isBlank()) {
            throw new UserPasswordNotNullException("Senha é obrigatoria");
        }

        Usuario novoUsuario = new Usuario(
                dto.nome(),
                dto.senha(),
                dto.email(),
                dto.telefone(),
                dto.userCargo(),
                UserStatus.ATIVO
        );

        Usuario usuarioSalvo = userRepository.save(novoUsuario);
        System.out.println("USUARIO CADASTRADO COM SUCESSO");

        // Gravando a movimentação na tabela genérica
        Movimentacao mov = new Movimentacao(
                AcaoMovimentacao.CRIACAO,
                TipoEntidade.USUARIO,
                novoUsuario.getUserId(),
                null,
                novoUsuario.getNome(),
                "NENHUM",
                userLogado
        );

        movimentacaoRepository.save(mov);
        System.out.println("MOVIMENTAÇÃO CADASTRADA COM SUCESSO");

        return usuarioSalvo;
    }

    @Transactional
    public Boolean atualizarDados(UUID id, UpdateUserDTO dto, String usuarioLogado) {

        Usuario userLogado = userRepository.findByNomeIgnoreCase(usuarioLogado).orElseThrow(() ->
                new UserNotFoundException("Usuario: " + usuarioLogado + " não foi encontrado"));

        Usuario usuarioAlterado = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuario não encontrado"));

        // Verificando se o Usuario que está logado tem o cargo necessario
        UserCargo cargo = userLogado.getUserCargo();
        if(cargo == null || (cargo != UserCargo.ADMINISTRADOR && cargo != UserCargo.DEV)) {
            throw new UserNotPermission("Usuario sem permissão");
        }


        // NOME
        if(dto.nome() != null && !dto.nome().isBlank() && !dto.nome().equalsIgnoreCase(usuarioAlterado.getNome())){
            usuarioAlterado.setNome(dto.nome().toLowerCase());
            movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.ATUALIZACAO, TipoEntidade.USUARIO, usuarioAlterado.getUserId(), null,usuarioAlterado.getNome(), "NOME", userLogado));
        }

        // SENHA
        if(dto.senha() != null && !dto.senha().isBlank() && !dto.senha().equals(usuarioAlterado.getSenha())){
            usuarioAlterado.setSenhaCriptografada(dto.senha());
            movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.ATUALIZACAO, TipoEntidade.USUARIO, usuarioAlterado.getUserId(), null, usuarioAlterado.getNome(), "SENHA", userLogado));
        }

        // EMAIL
        if(dto.email() != null && !dto.email().isBlank() && !dto.email().equalsIgnoreCase(usuarioAlterado.getEmail())) {
            usuarioAlterado.setEmail(dto.email().toLowerCase());
            movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.ATUALIZACAO, TipoEntidade.USUARIO, usuarioAlterado.getUserId(), null, usuarioAlterado.getNome(), "EMAIL", userLogado));
        }

        // TELEFONE
        if (dto.telefone() != null && !dto.telefone().isBlank() && !dto.telefone().equals(usuarioAlterado.getTelefone())) {
            usuarioAlterado.setTelefone(dto.telefone());
            movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.ATUALIZACAO, TipoEntidade.USUARIO, usuarioAlterado.getUserId(), null, usuarioAlterado.getNome(), "TELEFONE", userLogado));
        }

        // CARGO (Enums são comparados com !=)
        if(dto.userCargo() != null && dto.userCargo() != usuarioAlterado.getUserCargo()) {
            usuarioAlterado.setUserCargo(dto.userCargo());
            movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.ATUALIZACAO, TipoEntidade.USUARIO, usuarioAlterado.getUserId(), null, usuarioAlterado.getNome(), "CARGO", userLogado));
        }

        userRepository.save(usuarioAlterado);

        return true;
    }

    //METODO PARA DELETAR USUARIOS
    @Transactional
    public Boolean deletarUsuario(UUID id, String usuarioLogado) {

        Usuario userLogado = userRepository.findByNomeIgnoreCase(usuarioLogado).orElseThrow(() -> new UserNotFoundException("Usuario: " + usuarioLogado + " não encontrado"));
        Usuario usuarioAfetado = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Usuario: " + usuarioLogado + " não encontrado"));

        UserCargo cargoUserLogado = userLogado.getUserCargo();
        if(cargoUserLogado == null || (cargoUserLogado != UserCargo.ADMINISTRADOR && cargoUserLogado != UserCargo.DEV)) {
            throw new UserNotPermission("Usuario: " + usuarioLogado + " sem permissão");
        }

        usuarioAfetado.setUserStatus(UserStatus.INATIVO);
        userRepository.save(usuarioAfetado);

        movimentacaoRepository.save(new Movimentacao(AcaoMovimentacao.EXCLUSAO, TipoEntidade.USUARIO, usuarioAfetado.getUserId(), null, usuarioAfetado.getNome(), "NENHUM", userLogado));

        return true;
    }
}