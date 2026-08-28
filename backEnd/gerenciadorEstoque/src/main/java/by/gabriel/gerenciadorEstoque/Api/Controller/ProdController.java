package by.gabriel.gerenciadorEstoque.Api.Controller;

import by.gabriel.gerenciadorEstoque.Api.DTO.Produto.Consultas.SelectAllProdDTO;
import by.gabriel.gerenciadorEstoque.Api.DTO.Produto.ProdutoDTO;
import by.gabriel.gerenciadorEstoque.Api.DTO.Produto.UpdateProdDTO;
import by.gabriel.gerenciadorEstoque.Api.DTO.Response.ResponseDTO;
import by.gabriel.gerenciadorEstoque.Model.Produto.Produto;
import by.gabriel.gerenciadorEstoque.Services.ProdService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/produto")
public class ProdController {

    private final ProdService prodService;

    public  ProdController(ProdService prodService) {
        this.prodService = prodService;
    }

    @GetMapping("/listProd")
    public ResponseEntity<List<SelectAllProdDTO>> listAllProd() {

        List<SelectAllProdDTO> listados = prodService.listAllProd();

        return ResponseEntity.ok(listados);

    }


    @PostMapping("/cadastro")
    public ResponseEntity<ResponseDTO> cadastroProd(@RequestBody @Validated ProdutoDTO dto, @RequestHeader("X-Usuario-Logado") String usuarioLogado) {

        prodService.cadastrarProduto(dto, usuarioLogado);

        ResponseDTO sucesso = new ResponseDTO(

                true,
                "Produto cadastrado com sucesso",
                LocalDateTime.now().toString()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(sucesso);

    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> updateProd(@PathVariable Long id, @RequestBody @Validated UpdateProdDTO dto, @RequestHeader("X-Usuario-Logado") String username ) {

        Produto produtoAtualiazdo = prodService.updateProd(id, dto, username);

        ResponseDTO sucesso = new ResponseDTO(

                true,
                "Produto Atualizado com sucesso",
                LocalDateTime.now().toString()
        );

        return ResponseEntity.ok(sucesso);

    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteProd(@PathVariable Long id, @RequestHeader("X-Usuario-Logado") String username) {

        boolean produtoDeletado = prodService.deletarProd(id, username);


        if(produtoDeletado) {
            return ResponseEntity.ok(new ResponseDTO(
                    true,
                    "Produto deletado com sucesso!",
                    Instant.now().toString())
            );
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(
                    false,
                    "Algo deu errado na deelção do produto",
                    "BAD_DELETE", Instant.now().toString())
            );
        }

    }
}
