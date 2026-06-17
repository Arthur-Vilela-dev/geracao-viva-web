package br.org.conectageracaoviva.repository;

import br.org.conectageracaoviva.model.StatusVisita;
import br.org.conectageracaoviva.model.Visita;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitaRepository extends JpaRepository<Visita, Long> {
    boolean existsByDataHorarioAndStatus(LocalDateTime dataHorario, StatusVisita status);
    Optional<Visita> findByIdAndStatus(Long id, StatusVisita status);
}
