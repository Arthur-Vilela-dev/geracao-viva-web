package br.org.conectageracaoviva.controller;

import br.org.conectageracaoviva.model.Voluntario;
import br.org.conectageracaoviva.service.VoluntarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VoluntarioPerfilController {

    private final VoluntarioService voluntarioService;

    public VoluntarioPerfilController(VoluntarioService voluntarioService) {
        this.voluntarioService = voluntarioService;
    }

    @GetMapping("/voluntario/dados")
    public String editar(Authentication authentication, Model model) {
        Voluntario voluntario = voluntarioService.buscarPorEmail(authentication.getName());
        model.addAttribute("dadosForm", DadosVoluntarioForm.from(voluntario));
        return "voluntario-dados";
    }

    @PostMapping("/voluntario/dados")
    public String salvar(@Valid @ModelAttribute("dadosForm") DadosVoluntarioForm form,
                         BindingResult bindingResult,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "voluntario-dados";
        }

        voluntarioService.atualizarDados(
                authentication.getName(),
                form.getNome(),
                form.getTelefone(),
                form.getHabilidades()
        );

        redirectAttributes.addFlashAttribute("mensagem", "Seus dados foram atualizados.");
        return "redirect:/voluntario";
    }

    public static class DadosVoluntarioForm {

        @NotBlank(message = "Informe seu nome.")
        private String nome;

        @NotBlank(message = "Informe seu telefone.")
        private String telefone;

        private String habilidades;

        public static DadosVoluntarioForm from(Voluntario voluntario) {
            DadosVoluntarioForm form = new DadosVoluntarioForm();
            form.setNome(voluntario.getUsuario().getNome());
            form.setTelefone(voluntario.getTelefone());
            form.setHabilidades(voluntario.getHabilidades());
            return form;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getTelefone() {
            return telefone;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }

        public String getHabilidades() {
            return habilidades;
        }

        public void setHabilidades(String habilidades) {
            this.habilidades = habilidades;
        }
    }
}
