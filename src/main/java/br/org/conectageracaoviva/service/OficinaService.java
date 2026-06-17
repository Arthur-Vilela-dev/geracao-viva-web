package br.org.conectageracaoviva.service;

import br.org.conectageracaoviva.model.Crianca;
import br.org.conectageracaoviva.model.Matricula;
import br.org.conectageracaoviva.model.Oficina;
import br.org.conectageracaoviva.repository.CriancaRepository;
import br.org.conectageracaoviva.repository.MatriculaRepository;
import br.org.conectageracaoviva.repository.OficinaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OficinaService {

    private final OficinaRepository oficinaRepository;
    private final CriancaRepository criancaRepository;
    private final MatriculaRepository matriculaRepository;

    public OficinaService(OficinaRepository oficinaRepository,
                          CriancaRepository criancaRepository,
                          MatriculaRepository matriculaRepository) {
        this.oficinaRepository = oficinaRepository;
        this.criancaRepository = criancaRepository;
        this.matriculaRepository = matriculaRepository;
    }

    public Matricula matricularCrianca(Long criancaId, Long oficinaId) {
        Oficina oficina = oficinaRepository.findById(oficinaId)
                .orElseThrow(() -> new IllegalArgumentException("Oficina nao encontrada."));
        Crianca crianca = criancaRepository.findById(criancaId)
                .orElseThrow(() -> new IllegalArgumentException("Crianca nao encontrada."));

        if (matriculaRepository.existsByCriancaIdAndOficinaId(criancaId, oficinaId)) {
            throw new IllegalStateException("Esta crianca ja esta matriculada nesta oficina.");
        }

        long totalMatriculas = matriculaRepository.countByOficinaId(oficinaId);
        if (totalMatriculas >= oficina.getNumeroVagas()) {
            throw new IllegalStateException("Nao ha vagas disponiveis nesta oficina.");
        }

        Matricula matricula = new Matricula();
        matricula.setCrianca(crianca);
        matricula.setOficina(oficina);
        return matriculaRepository.save(matricula);
    }

    public List<Oficina> listarTodas() {
        return oficinaRepository.findAll();
    }

    public Oficina salvar(Oficina oficina) {
        return oficinaRepository.save(oficina);
    }

    public long contarMatriculas(Long oficinaId) {
        return matriculaRepository.countByOficinaId(oficinaId);
    }
}
