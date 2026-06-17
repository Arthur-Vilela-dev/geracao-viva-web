package br.org.conectageracaoviva.repository;

import br.org.conectageracaoviva.model.Doacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoacaoRepository extends JpaRepository<Doacao, Long> {
}
