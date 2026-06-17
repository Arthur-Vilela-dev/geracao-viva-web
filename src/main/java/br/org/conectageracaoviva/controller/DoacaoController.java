package br.org.conectageracaoviva.controller;

import br.org.conectageracaoviva.model.Doacao;
import br.org.conectageracaoviva.model.TipoDoacao;
import br.org.conectageracaoviva.service.DoacaoService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DoacaoController {

    private final DoacaoService doacaoService;

    public DoacaoController(DoacaoService doacaoService) {
        this.doacaoService = doacaoService;
    }

    @GetMapping("/doacoes/nova")
    public String nova(Model model) {
        if (!model.containsAttribute("doacaoForm")) {
            model.addAttribute("doacaoForm", new Doacao());
        }
        model.addAttribute("tiposDoacao", TipoDoacao.values());
        return "doacao-form";
    }

    @PostMapping("/doacoes/nova")
    public String registrar(@Valid Doacao doacaoForm,
                            BindingResult bindingResult,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tiposDoacao", TipoDoacao.values());
            return "doacao-form";
        }

        doacaoService.registrar(doacaoForm, authentication);
        redirectAttributes.addFlashAttribute("mensagem", "Doacao registrada. Muito obrigado pelo apoio!");
        return "redirect:/";
    }

    @GetMapping("/admin/doacoes")
    public String listarAdmin(Model model) {
        model.addAttribute("doacoes", doacaoService.listarTodas());
        return "admin-doacoes";
    }
}
