package br.org.conectageracaoviva.controller;

import br.org.conectageracaoviva.model.Visita;
import br.org.conectageracaoviva.service.VisitaService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VisitaController {

    private final VisitaService visitaService;

    public VisitaController(VisitaService visitaService) {
        this.visitaService = visitaService;
    }

    @GetMapping("/visitas/nova")
    public String nova(Model model) {
        if (!model.containsAttribute("visitaForm")) {
            model.addAttribute("visitaForm", new Visita());
        }
        model.addAttribute("horariosDisponiveis", visitaService.horariosDisponiveis(LocalDate.now().plusDays(1)));
        return "visita-form";
    }

    @PostMapping("/visitas/nova")
    public String agendar(@Valid Visita visitaForm,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("horariosDisponiveis", visitaService.horariosDisponiveis(LocalDate.now().plusDays(1)));
            return "visita-form";
        }

        try {
            visitaService.agendar(visitaForm);
            redirectAttributes.addFlashAttribute("mensagem", "Visita agendada com sucesso.");
            return "redirect:/";
        } catch (IllegalStateException | IllegalArgumentException erro) {
            model.addAttribute("erro", erro.getMessage());
            model.addAttribute("horariosDisponiveis", visitaService.horariosDisponiveis(LocalDate.now().plusDays(1)));
            return "visita-form";
        }
    }
}
