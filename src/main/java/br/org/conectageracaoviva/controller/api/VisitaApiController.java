package br.org.conectageracaoviva.controller.api;

import br.org.conectageracaoviva.model.StatusVisita;
import br.org.conectageracaoviva.model.Visita;
import br.org.conectageracaoviva.service.VisitaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/visitas")
public class VisitaApiController {

    private final VisitaService visitaService;

    public VisitaApiController(VisitaService visitaService) {
        this.visitaService = visitaService;
    }

    @PostMapping
    public ResponseEntity<VisitaResponse> agendar(@Valid @RequestBody CriarVisitaRequest request) {
        // Converte o JSON recebido em uma entidade que sera salva no banco.
        Visita visita = new Visita();
        visita.setNomeVisitante(request.nomeVisitante());
        visita.setEmailVisitante(request.emailVisitante());
        visita.setTelefoneVisitante(request.telefoneVisitante());
        visita.setDataHorario(request.dataHorario());

        Visita visitaSalva = visitaService.agendar(visita);

        return ResponseEntity.status(HttpStatus.CREATED).body(VisitaResponse.from(visitaSalva));
    }

    @GetMapping("/disponibilidade")
    public List<LocalDateTime> disponibilidade(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate data) {
        return visitaService.horariosDisponiveis(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        visitaService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    public record CriarVisitaRequest(
            @NotBlank(message = "Informe o nome do visitante.")
            String nomeVisitante,

            @Email(message = "Informe um email valido.")
            String emailVisitante,

            @NotBlank(message = "Informe o telefone do visitante.")
            String telefoneVisitante,

            @NotNull(message = "Informe a data e horario da visita.")
            LocalDateTime dataHorario
    ) {
    }

    public record VisitaResponse(
            Long id,
            String nomeVisitante,
            String emailVisitante,
            String telefoneVisitante,
            LocalDateTime dataHorario,
            StatusVisita status
    ) {
        public static VisitaResponse from(Visita visita) {
            return new VisitaResponse(
                    visita.getId(),
                    visita.getNomeVisitante(),
                    visita.getEmailVisitante(),
                    visita.getTelefoneVisitante(),
                    visita.getDataHorario(),
                    visita.getStatus()
            );
        }
    }
}
