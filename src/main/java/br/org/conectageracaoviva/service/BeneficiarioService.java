package br.org.conectageracaoviva.service;

import br.org.conectageracaoviva.model.Crianca;
import br.org.conectageracaoviva.model.Matricula;
import br.org.conectageracaoviva.model.Responsavel;
import br.org.conectageracaoviva.repository.CriancaRepository;
import br.org.conectageracaoviva.repository.ResponsavelRepository;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BeneficiarioService {

    private final ResponsavelRepository responsavelRepository;
    private final CriancaRepository criancaRepository;
    private final OficinaService oficinaService;

    public BeneficiarioService(ResponsavelRepository responsavelRepository,
                               CriancaRepository criancaRepository,
                               OficinaService oficinaService) {
        this.responsavelRepository = responsavelRepository;
        this.criancaRepository = criancaRepository;
        this.oficinaService = oficinaService;
    }

    @Transactional
    public Matricula cadastrarCriancaComResponsavelEOficina(DadosBeneficiario dados) {
        Responsavel responsavel = new Responsavel();
        responsavel.setNome(dados.nomeResponsavel());
        responsavel.setTelefone(dados.telefoneResponsavel());
        responsavel.setCpf(dados.cpfResponsavel());
        Responsavel responsavelSalvo = responsavelRepository.save(responsavel);

        Crianca crianca = new Crianca();
        crianca.setNome(dados.nomeCrianca());
        crianca.setDataNascimento(dados.dataNascimento());
        crianca.setResponsavel(responsavelSalvo);
        Crianca criancaSalva = criancaRepository.save(crianca);

        // RN-002: o OficinaService verifica se a oficina possui vagas antes de matricular.
        return oficinaService.matricularCrianca(criancaSalva.getId(), dados.oficinaId());
    }

    public record DadosBeneficiario(
            String nomeResponsavel,
            String telefoneResponsavel,
            String cpfResponsavel,
            String nomeCrianca,
            LocalDate dataNascimento,
            Long oficinaId
    ) {
    }
}
