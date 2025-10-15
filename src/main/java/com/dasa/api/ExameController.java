package com.dasa.api;

import com.dasa.controller.dao.ExameDao;
import com.dasa.controller.dao.jdbc.JdbcExameDao;
import com.dasa.model.domain.Exame;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Exames", description = "Consulta de exames")
@RestController
@RequestMapping("/api/exames")
public class ExameController {

    private final ExameDao dao = new JdbcExameDao();

    @Operation(summary = "Listar exames")
    @GetMapping
    public List<Exame> listar() {
        return dao.listarTodos();
    }

    @Operation(summary = "Buscar exame por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Exame> buscarPorId(@PathVariable int id) {
        Exame e = dao.buscarPorId(id);
        return e != null ? ResponseEntity.ok(e) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar exame por nome")
    @GetMapping("/nome/{nome}")
    public ResponseEntity<Exame> buscarPorNome(@PathVariable String nome) {
        Exame e = dao.buscarPorNome(nome);
        return e != null ? ResponseEntity.ok(e) : ResponseEntity.notFound().build();
    }
}