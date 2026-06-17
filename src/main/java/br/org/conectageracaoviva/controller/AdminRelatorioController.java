package br.org.conectageracaoviva.controller;

import br.org.conectageracaoviva.model.StatusVoluntario;
import br.org.conectageracaoviva.repository.CriancaRepository;
import br.org.conectageracaoviva.repository.DoacaoRepository;
import br.org.conectageracaoviva.repository.EventoRepository;
import br.org.conectageracaoviva.repository.OficinaRepository;
import br.org.conectageracaoviva.repository.VisitaRepository;
import br.org.conectageracaoviva.service.VoluntarioService;
import java.math.BigDecimal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminRelatorioController {

    private final CriancaRepository criancaRepository;
    private final DoacaoRepository doacaoRepository;
    private final EventoRepository eventoRepository;
    private final OficinaRepository oficinaRepository;
    private final VisitaRepository visitaRepository;
    private final VoluntarioService voluntarioService;

    public AdminRelatorioController(CriancaRepository criancaRepository,
                                    DoacaoRepository doacaoRepository,
                                    EventoRepository eventoRepository,
                                    OficinaRepository oficinaRepository,
                                    VisitaRepository visitaRepository,
                                    VoluntarioService voluntarioService) {
        this.criancaRepository = criancaRepository;
        this.doacaoRepository = doacaoRepository;
        this.eventoRepository = eventoRepository;
        this.oficinaRepository = oficinaRepository;
        this.visitaRepository = visitaRepository;
        this.voluntarioService = voluntarioService;
    }

    @GetMapping("/admin/relatorios")
    public String relatorios(Model model) {
        var voluntarios = voluntarioService.listarTodos();
        BigDecimal totalDoacoes = doacaoRepository.findAll()
                .stream()
                .map(doacao -> doacao.getValor() == null ? BigDecimal.ZERO : doacao.getValor())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("totalBeneficiarios", criancaRepository.count());
        model.addAttribute("totalVoluntarios", voluntarios.size());
        model.addAttribute("voluntariosAtivos", voluntarios.stream().filter(v -> v.getStatus() == StatusVoluntario.ATIVO).count());
        model.addAttribute("voluntariosPendentes", voluntarios.stream().filter(v -> v.getStatus() == StatusVoluntario.PENDENTE).count());
        model.addAttribute("totalOficinas", oficinaRepository.count());
        model.addAttribute("totalEventos", eventoRepository.count());
        model.addAttribute("totalVisitas", visitaRepository.count());
        model.addAttribute("quantidadeDoacoes", doacaoRepository.count());
        model.addAttribute("totalDoacoes", totalDoacoes);
        return "admin-relatorios";
    }
}
