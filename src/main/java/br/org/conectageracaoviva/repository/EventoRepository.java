package br.org.conectageracaoviva.repository;

import br.org.conectageracaoviva.model.Evento;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByDataHorarioAfterOrderByDataHorarioAsc(LocalDateTime data);
}
