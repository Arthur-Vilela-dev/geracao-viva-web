package br.org.conectageracaoviva.repository;

import br.org.conectageracaoviva.model.Perfil;
import br.org.conectageracaoviva.model.PerfilNome;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByNome(PerfilNome nome);
}
