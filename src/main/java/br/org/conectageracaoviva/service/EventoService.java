package br.org.conectageracaoviva.service;

import br.org.conectageracaoviva.model.Evento;
import br.org.conectageracaoviva.repository.EventoRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public List<Evento> proximosEventos() {
        return eventoRepository.findByDataHorarioAfterOrderByDataHorarioAsc(LocalDateTime.now());
    }

    public List<Evento> listarTodos() {
        return eventoRepository.findAll();
    }

    public Evento salvar(Evento evento) {
        return eventoRepository.save(evento);
    }
}
