package by.gabriel.gerenciadorEstoque.Repository.Servicos;

import by.gabriel.gerenciadorEstoque.Api.DTO.Servicos.Consultas.ServicosSelectDTO;
import by.gabriel.gerenciadorEstoque.Enum.Servicos.ServicosStatus;
import by.gabriel.gerenciadorEstoque.Model.Servicos.Servicos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicosRepository extends JpaRepository<Servicos, Long> {

    // Busca rápida para evitar serviços duplicados
    boolean existsByDescServicoIgnoreCaseAndStatus(String descServico, ServicosStatus status);

    // Listagem otimizada direto pro DTO
    @Query("SELECT new by.gabriel.gerenciadorEstoque.Api.DTO.Servicos.Consultas.ServicosSelectDTO(" +
            "s.servicosId, s.descServico, s.precoServico, s.status) " +
            "FROM Servicos s WHERE s.status = :status " +
            "ORDER BY s.descServico ASC")
    List<ServicosSelectDTO> findByStatusCustom(@Param("status") ServicosStatus status);
}