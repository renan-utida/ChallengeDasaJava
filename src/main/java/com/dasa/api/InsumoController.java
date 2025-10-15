package com.dasa.api;

import com.dasa.controller.dao.InsumoDao;
import com.dasa.controller.dao.jdbc.JdbcInsumoDao;
import com.dasa.model.domain.Insumo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ResponseEntity<Map<String,Object>> atualizarQuantidade(@PathVariable int id, @RequestParam int valor) {
        dao.atualizarQuantidade(id, valor);
        return ResponseEntity.ok(Map.of("id", id, "quantidade", valor));
    }

    @Operation(summary = "Adicionar ao estoque")
    @PostMapping("/{id}/adicionar")
    public ResponseEntity<Map<String,Object>> adicionar(@PathVariable int id, @RequestParam int quantidade) {
        boolean ok = dao.adicionarQuantidade(id, quantidade);
        return ok ? ResponseEntity.ok(Map.of("id", id, "adicionado", quantidade))
                : ResponseEntity.badRequest().body(Map.of("erro", "Quantidade excede o máximo permitido"));
    }

    @Operation(summary = "Remover do estoque")
    @PostMapping("/{id}/remover")
    public ResponseEntity<Map<String,Object>> remover(@PathVariable int id, @RequestParam int quantidade) {
        boolean ok = dao.removerQuantidade(id, quantidade);
        return ok ? ResponseEntity.ok(Map.of("id", id, "removido", quantidade))
                : ResponseEntity.badRequest().body(Map.of("erro", "Quantidade ficaria negativa"));
    }
}