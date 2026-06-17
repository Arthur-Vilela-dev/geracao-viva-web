package br.org.conectageracaoviva.repository;

import br.org.conectageracaoviva.model.Voluntario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {
    Optional<Voluntario> findByUsuarioEmail(String email);
}
