package br.org.conectageracaoviva.controller;

import br.org.conectageracaoviva.model.Crianca;
import br.org.conectageracaoviva.model.StatusVoluntario;
import br.org.conectageracaoviva.model.Voluntario;
import br.org.conectageracaoviva.repository.CriancaRepository;
import br.org.conectageracaoviva.service.EventoService;
import br.org.conectageracaoviva.service.OficinaService;
import br.org.conectageracaoviva.service.VoluntarioService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PaginaController {

    private final VoluntarioService voluntarioService;
    private final CriancaRepository criancaRepository;
    private final EventoService eventoService;
    private final OficinaService oficinaService;

    public PaginaController(VoluntarioService voluntarioService,
                            CriancaRepository criancaRepository,
                            EventoService eventoService,
                            OficinaService oficinaService) {
        this.voluntarioService = voluntarioService;
        this.criancaRepository = criancaRepository;
        this.eventoService = eventoService;
        this.oficinaService = oficinaService;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("proximosEventos", eventoService.proximosEventos());
        model.addAttribute("oficinas", oficinaService.listarTodas());
        // Retorna o arquivo src/main/resources/templates/index.html.
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String perfil, Model model) {
        model.addAttribute("perfilSelecionado", perfil);
        // Retorna o arquivo src/main/resources/templates/login.html.
        return "login";
    }

    @GetMapping("/admin")
    public String dashboardAdmin(Model model) {
        List<Voluntario> voluntarios = voluntarioService.listarTodos();

        long pendentes = voluntarios.stream()
                .filter(voluntario -> voluntario.getStatus() == StatusVoluntario.PENDENTE)
                .count();

        long ativos = voluntarios.stream()
                .filter(voluntario -> voluntario.getStatus() == StatusVoluntario.ATIVO)
                .count();

        model.addAttribute("voluntarios", voluntarios);
        model.addAttribute("totalVoluntarios", voluntarios.size());
        model.addAttribute("voluntariosPendentes", pendentes);
        model.addAttribute("voluntariosAtivos", ativos);
        model.addAttribute("totalBeneficiarios", criancaRepository.count());
        return "dashboard-admin";
    }

    @PostMapping("/admin/voluntarios/{id}/status")
    public String alterarStatusVoluntario(@PathVariable Long id,
                                          @RequestParam StatusVoluntario status,
                                          RedirectAttributes redirectAttributes) {
        // RN-001: esta rota fica em /admin/**, portanto apenas administradores podem alterar status.
        voluntarioService.alterarStatus(id, status);

        if (status == StatusVoluntario.REPROVADO) {
            redirectAttributes.addFlashAttribute("voluntarioReprovadoId", id);
            redirectAttributes.addFlashAttribute("mensagem", "Voluntario reprovado. A exclusao automatica comecara em 30 segundos.");
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "Status do voluntario atualizado.");
        }

        return "redirect:/admin";
    }

    @PostMapping("/admin/voluntarios/{id}/excluir")
    public String excluirVoluntario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // RN-001: somente administradores podem excluir voluntarios da gestao.
        voluntarioService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagem", "Voluntario removido da gestao.");
        return "redirect:/admin";
    }

    @GetMapping("/voluntario")
    public String dashboardVoluntario(Model model) {
        model.addAttribute("oficinas", oficinaService.listarTodas());
        model.addAttribute("proximosEventos", eventoService.proximosEventos());
        return "dashboard-voluntario";
    }

    @GetMapping("/comunidade")
    public String dashboardComunidade(Model model) {
        // RN-003: comunidade nao visualiza dados sensiveis de menores.
        // A lista completa fica apenas no painel do administrador.
        model.addAttribute("totalBeneficiarios", criancaRepository.count());
        return "dashboard-comunidade";
    }

}
