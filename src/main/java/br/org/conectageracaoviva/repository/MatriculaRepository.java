package br.org.conectageracaoviva.repository;

import br.org.conectageracaoviva.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    long countByOficinaId(Long oficinaId);
    boolean existsByCriancaIdAndOficinaId(Long criancaId, Long oficinaId);
}
