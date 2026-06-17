package br.org.conectageracaoviva.service;

import br.org.conectageracaoviva.model.StatusVisita;
import br.org.conectageracaoviva.model.Visita;
import br.org.conectageracaoviva.repository.VisitaRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class VisitaService {

    private final VisitaRepository visitaRepository;

    public VisitaService(VisitaRepository visitaRepository) {
        this.visitaRepository = visitaRepository;
    }

    public Visita agendar(Visita visita) {
        if (visita.getDataHorario() == null) {
            throw new IllegalArgumentException("Informe a data e horario da visita.");
        }

        boolean ocupado = visitaRepository.existsByDataHorarioAndStatus(
                visita.getDataHorario(),
                StatusVisita.AGENDADA
        );

        if (ocupado) {
            throw new IllegalStateException("Este horario ja esta ocupado.");
        }

        visita.setStatus(StatusVisita.AGENDADA);
        return visitaRepository.save(visita);
    }

    public List<LocalDateTime> horariosDisponiveis(LocalDate data) {
        // Horarios simples para aprendizado: 09h, 10h, 14h e 15h.
        List<LocalTime> horariosPadrao = List.of(
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                LocalTime.of(14, 0),
                LocalTime.of(15, 0)
        );

        List<LocalDateTime> disponiveis = new ArrayList<>();
        for (LocalTime horario : horariosPadrao) {
            LocalDateTime dataHorario = LocalDateTime.of(data, horario);
            boolean ocupado = visitaRepository.existsByDataHorarioAndStatus(dataHorario, StatusVisita.AGENDADA);
            if (!ocupado) {
                disponiveis.add(dataHorario);
            }
        }
        return disponiveis;
    }

    public void cancelar(Long id) {
        Visita visita = visitaRepository.findByIdAndStatus(id, StatusVisita.AGENDADA)
                .orElseThrow(() -> new IllegalArgumentException("Visita agendada nao encontrada."));
        visita.setStatus(StatusVisita.CANCELADA);
        visitaRepository.save(visita);
    }
}
