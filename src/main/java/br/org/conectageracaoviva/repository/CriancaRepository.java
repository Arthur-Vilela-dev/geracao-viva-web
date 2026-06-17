package br.org.conectageracaoviva.repository;

import br.org.conectageracaoviva.model.Crianca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriancaRepository extends JpaRepository<Crianca, Long> {
}
