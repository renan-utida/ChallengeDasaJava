package com.dasa.api;

import com.dasa.controller.dao.PacienteDao;
import com.dasa.controller.dao.jdbc.JdbcPacienteDao;
import com.dasa.dto.PacienteRequestDTO;
import com.dasa.dto.PacienteResponseDTO;
import com.dasa.model.domain.Paciente;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Pacientes", description = "Operações de CRUD para pacientes")
@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteDao dao = new JdbcPacienteDao();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Operation(summary = "Listar pacientes")
    @GetMapping
    public List<PacienteResponseDTO> listar() {
        return dao.listarTodos().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Buscar paciente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> buscarPorId(@PathVariable int id) {
        Paciente p = dao.buscarPorId(id);
        return p != null ? ResponseEntity.ok(toResponseDTO(p)) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar paciente por CPF")
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<PacienteResponseDTO> buscarPorCpf(@PathVariable String cpf) {
        // Remove formatação do CPF
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        Paciente p = dao.buscarPorCpf(cpfLimpo);
        return p != null ? ResponseEntity.ok(toResponseDTO(p)) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Criar paciente")
    @PostMapping
    public ResponseEntity<PacienteResponseDTO> criar(@Valid @RequestBody PacienteRequestDTO dto) {
        // Converte DTO para Model
        Paciente paciente = toModel(dto);

        // Salva no banco
        Long id = dao.salvar(paciente);
        paciente.setId(id.intValue());

        return ResponseEntity.created(URI.create("/api/pacientes/" + id))
                .body(toResponseDTO(paciente));
    }

    @Operation(summary = "Atualizar paciente")
    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> atualizar(
            @PathVariable int id,
            @Valid @RequestBody PacienteRequestDTO dto) {

        Paciente atual = dao.buscarPorId(id);
        if (atual == null) {
            return ResponseEntity.notFound().build();
        }

        // Atualiza campos mantendo o status original
        Paciente atualizado = toModel(dto);
        atualizado.setId(id);
        atualizado.setStatusPaciente(atual.getStatusPaciente()); // Mantém status

        dao.atualizar(atualizado);

        return ResponseEntity.ok(toResponseDTO(atualizado));
    }

    @Operation(summary = "Excluir paciente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable int id) {
        Paciente atual = dao.buscarPorId(id);
        if (atual == null) {
            return ResponseEntity.notFound().build();
        }
        dao.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // ========== MÉTODOS AUXILIARES (MAPPERS) ==========

    /**
     * Converte DTO de Request para Model
     */
    private Paciente toModel(PacienteRequestDTO dto) {
        Paciente p = new Paciente();
        p.setNomeCompleto(dto.getNomeCompleto().trim());
        p.setCpf(dto.getCpf().replaceAll("[^0-9]", "")); // Remove formatação
        p.setDataNascimento(LocalDate.parse(dto.getDataNascimento(), FORMATTER));
        p.setConvenio(dto.getConvenio());
        p.setPreferencial(dto.getPreferencial());
        p.setStatusPaciente("Ativo"); // Default para novos pacientes
        return p;
    }

    /**
     * Converte Model para DTO de Response
     */
    private PacienteResponseDTO toResponseDTO(Paciente p) {
        return new PacienteResponseDTO(
                p.getId(),
                p.getNomeCompleto(),
                p.getCpfFormatado(),
                p.getDataNascimento().format(FORMATTER),
                p.isConvenio(),
                p.isPreferencial(),
                p.getStatusPaciente()
        );
    }
}