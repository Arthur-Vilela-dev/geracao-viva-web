package br.org.conectageracaoviva.controller;

import br.org.conectageracaoviva.repository.CriancaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminBeneficiarioController {

    private final CriancaRepository criancaRepository;

    public AdminBeneficiarioController(CriancaRepository criancaRepository) {
        this.criancaRepository = criancaRepository;
    }

    @GetMapping("/admin/beneficiarios")
    public String beneficiarios(Model model) {
        model.addAttribute("beneficiarios", criancaRepository.findAll());
        return "admin-beneficiarios";
    }
}
