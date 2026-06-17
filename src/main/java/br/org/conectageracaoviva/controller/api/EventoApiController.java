package br.org.conectageracaoviva.controller.api;

import br.org.conectageracaoviva.model.Evento;
import br.org.conectageracaoviva.service.EventoService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/eventos")
public class EventoApiController {

    private final EventoService eventoService;

    public EventoApiController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping
    public List<EventoResponse> listarProximos() {
        return eventoService.proximosEventos()
                .stream()
                .map(EventoResponse::from)
                .toList();
    }

    public record EventoResponse(
            Long id,
            String nome,
            String descricao,
            LocalDateTime dataHorario,
            int quantidadeVagas
    ) {
        public static EventoResponse from(Evento evento) {
            return new EventoResponse(
                    evento.getId(),
                    evento.getNome(),
                    evento.getDescricao(),
                    evento.getDataHorario(),
                    evento.getQuantidadeVagas()
            );
        }
    }
}
