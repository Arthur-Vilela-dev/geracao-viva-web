package br.org.conectageracaoviva.controller;

import br.org.conectageracaoviva.model.Oficina;
import br.org.conectageracaoviva.service.OficinaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminOficinaController {

    private final OficinaService oficinaService;

    public AdminOficinaController(OficinaService oficinaService) {
        this.oficinaService = oficinaService;
    }

    @GetMapping("/admin/oficinas")
    public String listar(Model model) {
        prepararTela(model, new OficinaForm());
        return "admin-oficinas";
    }

    @PostMapping("/admin/oficinas")
    public String cadastrar(@Valid @ModelAttribute("oficinaForm") OficinaForm form,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            prepararTela(model, form);
            return "admin-oficinas";
        }

        Oficina oficina = new Oficina();
        oficina.setNome(form.getNome());
        oficina.setDescricao(form.getDescricao());
        oficina.setNumeroVagas(form.getNumeroVagas());
        oficina.setHorarios(form.getHorarios());
        oficina.setInstrutorResponsavel(form.getInstrutorResponsavel());

        oficinaService.salvar(oficina);
        redirectAttributes.addFlashAttribute("mensagem", "Oficina cadastrada com sucesso.");
        return "redirect:/admin/oficinas";
    }

    private void prepararTela(Model model, OficinaForm form) {
        List<Oficina> oficinas = oficinaService.listarTodas();
        model.addAttribute("oficinaForm", form);
        model.addAttribute("oficinas", oficinas);
        model.addAttribute("totalOficinas", oficinas.size());
        model.addAttribute("oficinaService", oficinaService);
    }

    public static class OficinaForm {

        @NotBlank(message = "Informe o nome da oficina.")
        private String nome;

        private String descricao;

        @Min(value = 1, message = "A oficina precisa ter pelo menos 1 vaga.")
        private int numeroVagas = 1;

        @NotBlank(message = "Informe os horarios.")
        private String horarios;

        @NotBlank(message = "Informe o instrutor responsavel.")
        private String instrutorResponsavel;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public int getNumeroVagas() {
            return numeroVagas;
        }

        public void setNumeroVagas(int numeroVagas) {
            this.numeroVagas = numeroVagas;
        }

        public String getHorarios() {
            return horarios;
        }

        public void setHorarios(String horarios) {
            this.horarios = horarios;
        }

        public String getInstrutorResponsavel() {
            return instrutorResponsavel;
        }

        public void setInstrutorResponsavel(String instrutorResponsavel) {
            this.instrutorResponsavel = instrutorResponsavel;
        }
    }
}
