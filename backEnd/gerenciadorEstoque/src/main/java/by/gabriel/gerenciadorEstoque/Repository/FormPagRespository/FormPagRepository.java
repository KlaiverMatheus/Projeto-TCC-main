package by.gabriel.gerenciadorEstoque.Repository.FormPagRespository;

import by.gabriel.gerenciadorEstoque.Api.DTO.FormPagDTO.Consultas.SelectFormPagStatusDTO;
import by.gabriel.gerenciadorEstoque.Model.FormaPag.FormaPagto;
import by.gabriel.gerenciadorEstoque.Enum.FormaPag.FormaPagStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormPagRepository extends JpaRepository<FormaPagto, Long> {

    Optional<FormaPagto> findByDescricaoIgnoreCase(String descricao);

    boolean existsByDescricaoIgnoreCaseAndStatus(String descricao, FormaPagStatus status);

    @Query("""
           SELECT new by.gabriel.gerenciadorEstoque.Api.DTO.FormPagDTO.Consultas.SelectFormPagStatusDTO(
               fp.id, 
               fp.descricao, 
               fp.status
           )
           FROM FormaPagto fp
           WHERE fp.status = :status
           ORDER BY fp.descricao ASC
           """)
    List<SelectFormPagStatusDTO> findByStatusCustom(@Param("status") FormaPagStatus status);
}
