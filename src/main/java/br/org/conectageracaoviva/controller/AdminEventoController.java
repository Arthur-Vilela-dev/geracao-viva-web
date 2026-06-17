package br.org.conectageracaoviva.controller;

import br.org.conectageracaoviva.model.Evento;
import br.org.conectageracaoviva.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminEventoController {

    private final EventoService eventoService;

    public AdminEventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @GetMapping("/admin/eventos")
    public String eventos(Model model) {
        if (!model.containsAttribute("eventoForm")) {
            model.addAttribute("eventoForm", new Evento());
        }

        model.addAttribute("eventos", eventoService.listarTodos());
        return "admin-eventos";
    }

    @PostMapping("/admin/eventos")
    public String salvar(@Valid Evento eventoForm,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eventos", eventoService.listarTodos());
            return "admin-eventos";
        }

        eventoService.salvar(eventoForm);
        redirectAttributes.addFlashAttribute("mensagem", "Evento cadastrado com sucesso.");
        return "redirect:/admin/eventos";
    }
}
