package com.dasa.api;

import com.dasa.controller.dao.PacienteDao;
import com.dasa.controller.dao.jdbc.JdbcPacienteDao;
import com.dasa.model.domain.Paciente;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Pacientes", description = "Operações de CRUD para pacientes")
@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteDao dao = new JdbcPacienteDao();

    @Operation(summary = "Listar pacientes")
    @GetMapping
    public List<Paciente> listar() {
        return dao.listarTodos();
    }

    @Operation(summary = "Buscar paciente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPorId(@PathVariable int id) {
        Paciente p = dao.buscarPorId(id);
        return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar paciente por CPF")
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Paciente> buscarPorCpf(@PathVariable String cpf) {
        Paciente p = dao.buscarPorCpf(cpf);
        return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Criar paciente")
    @PostMapping
    public ResponseEntity<Paciente> criar(@RequestBody Paciente paciente) {
        Long id = dao.salvar(paciente);
        // alguns DAOs usam int; garantir que o ID volte no objeto
        if (id != null) paciente.setId(id.intValue());
        return ResponseEntity.created(URI.create("/api/pacientes/" + paciente.getId())).body(paciente);
    }

    @Operation(summary = "Atualizar paciente")
    @PutMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable int id, @RequestBody Paciente paciente) {
        Paciente atual = dao.buscarPorId(id);
        if (atual == null) return ResponseEntity.notFound().build();
        paciente.setId(id);
        dao.atualizar(paciente);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Excluir paciente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable int id) {
        Paciente atual = dao.buscarPorId(id);
        if (atual == null) return ResponseEntity.notFound().build();
        dao.excluir(id);
        return ResponseEntity.noContent().build();
    }
}