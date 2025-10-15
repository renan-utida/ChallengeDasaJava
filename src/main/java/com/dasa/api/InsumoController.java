package com.dasa.api;

import com.dasa.controller.dao.InsumoDao;
import com.dasa.controller.dao.jdbc.JdbcInsumoDao;
import com.dasa.dto.InsumoUpdateDTO;
import com.dasa.model.domain.Insumo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Insumos", description = "Consulta e ajuste de estoque de insumos")
@RestController
@RequestMapping("/api/insumos")
public class InsumoController {

    private final InsumoDao dao = new JdbcInsumoDao();

    @Operation(summary = "Listar todos os insumos")
    @GetMapping
    public List<Insumo> listarTodos() {
        return dao.listarTodos();
    }

    @Operation(summary = "Buscar insumo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Insumo> buscarPorId(@PathVariable int id) {
        Insumo i = dao.buscarPorId(id);
        return i != null ? ResponseEntity.ok(i) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar insumo por código de barras")
    @GetMapping("/codigo/{codigoBarras}")
    public ResponseEntity<Insumo> buscarPorCodigo(@PathVariable int codigoBarras) {
        Insumo i = dao.buscarPorCodigoBarras(codigoBarras);
        return i != null ? ResponseEntity.ok(i) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Listar insumos por tipo")
    @GetMapping("/tipo/{tipo}")
    public List<Insumo> listarPorTipo(@PathVariable String tipo) {
        return dao.listarPorTipo(tipo);
    }

    @Operation(summary = "Listar insumos associados a um exame")
    @GetMapping("/exame/{nomeExame}")
    public List<Insumo> listarPorExame(@PathVariable String nomeExame) {
        return dao.listarPorExame(nomeExame);
    }

    @Operation(summary = "Atualizar quantidade absoluta")
    @PutMapping("/{id}/quantidade")
    public ResponseEntity<Map<String, Object>> atualizarQuantidade(
            @PathVariable int id,
            @Valid @RequestBody InsumoUpdateDTO dto) {

        Insumo insumo = dao.buscarPorId(id);
        if (insumo == null) {
            return ResponseEntity.notFound().build();
        }

        dao.atualizarQuantidade(id, dto.getQuantidade());

        return ResponseEntity.ok(Map.of(
                "id", id,
                "quantidade", dto.getQuantidade(),
                "nome", insumo.getNome()
        ));
    }

    @Operation(summary = "Adicionar ao estoque")
    @PostMapping("/{id}/adicionar")
    public ResponseEntity<Map<String, Object>> adicionar(
            @PathVariable int id,
            @RequestParam @Min(value = 1, message = "Quantidade deve ser no mínimo 1") int quantidade) {

        Insumo insumo = dao.buscarPorId(id);
        if (insumo == null) {
            return ResponseEntity.notFound().build();
        }

        boolean ok = dao.adicionarQuantidade(id, quantidade);

        if (!ok) {
            return ResponseEntity.badRequest().body(Map.of(
                    "erro", "Quantidade excede o máximo permitido",
                    "quantidadeAtual", insumo.getQuantidadeDisponivel(),
                    "quantidadeMaxima", insumo.getQuantidadeMaxima()
            ));
        }

        // Atualiza o objeto
        insumo = dao.buscarPorId(id);

        return ResponseEntity.ok(Map.of(
                "id", id,
                "adicionado", quantidade,
                "quantidadeAtual", insumo.getQuantidadeDisponivel(),
                "nome", insumo.getNome()
        ));
    }

    @Operation(summary = "Remover do estoque")
    @PostMapping("/{id}/remover")
    public ResponseEntity<Map<String, Object>> remover(
            @PathVariable int id,
            @RequestParam @Min(value = 1, message = "Quantidade deve ser no mínimo 1") int quantidade) {

        Insumo insumo = dao.buscarPorId(id);
        if (insumo == null) {
            return ResponseEntity.notFound().build();
        }

        boolean ok = dao.removerQuantidade(id, quantidade);

        if (!ok) {
            return ResponseEntity.badRequest().body(Map.of(
                    "erro", "Quantidade insuficiente em estoque",
                    "quantidadeDisponivel", insumo.getQuantidadeDisponivel(),
                    "quantidadeSolicitada", quantidade
            ));
        }

        // Atualiza o objeto
        insumo = dao.buscarPorId(id);

        return ResponseEntity.ok(Map.of(
                "id", id,
                "removido", quantidade,
                "quantidadeAtual", insumo.getQuantidadeDisponivel(),
                "nome", insumo.getNome()
        ));
    }
}