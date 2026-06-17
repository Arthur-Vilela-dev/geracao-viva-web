package br.org.conectageracaoviva.controller;

import br.org.conectageracaoviva.service.BeneficiarioService;
import br.org.conectageracaoviva.service.BeneficiarioService.DadosBeneficiario;
import br.org.conectageracaoviva.repository.OficinaRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BeneficiarioController {

    private final BeneficiarioService beneficiarioService;
    private final OficinaRepository oficinaRepository;

    public BeneficiarioController(BeneficiarioService beneficiarioService,
                                  OficinaRepository oficinaRepository) {
        this.beneficiarioService = beneficiarioService;
        this.oficinaRepository = oficinaRepository;
    }

    @GetMapping("/beneficiarios/novo")
    public String novo(Model model) {
        prepararFormulario(model, new BeneficiarioForm());
        return "beneficiario-form";
    }

    @PostMapping("/beneficiarios/novo")
    public String cadastrar(@Valid @ModelAttribute("beneficiarioForm") BeneficiarioForm form,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            prepararFormulario(model, form);
            return "beneficiario-form";
        }

        try {
            beneficiarioService.cadastrarCriancaComResponsavelEOficina(new DadosBeneficiario(
                    form.getNomeResponsavel(),
                    form.getTelefoneResponsavel(),
                    form.getCpfResponsavel(),
                    form.getNomeCrianca(),
                    form.getDataNascimento(),
                    form.getOficinaId()
            ));
        } catch (IllegalStateException | IllegalArgumentException exception) {
            model.addAttribute("erro", exception.getMessage());
            prepararFormulario(model, form);
            return "beneficiario-form";
        }

        redirectAttributes.addFlashAttribute("mensagem", "Beneficiario cadastrado e matriculado na oficina.");
        return "redirect:/comunidade";
    }

    private void prepararFormulario(Model model, BeneficiarioForm form) {
        model.addAttribute("beneficiarioForm", form);
        model.addAttribute("oficinas", oficinaRepository.findAll());
    }

    public static class BeneficiarioForm {

        @NotBlank(message = "Informe o nome do responsavel.")
        private String nomeResponsavel;

        @NotBlank(message = "Informe o telefone do responsavel.")
        private String telefoneResponsavel;

        private String cpfResponsavel;

        @NotBlank(message = "Informe o nome da crianca ou adolescente.")
        private String nomeCrianca;

        @NotNull(message = "Informe a data de nascimento.")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate dataNascimento;

        @NotNull(message = "Escolha uma oficina.")
        private Long oficinaId;

        public String getNomeResponsavel() {
            return nomeResponsavel;
        }

        public void setNomeResponsavel(String nomeResponsavel) {
            this.nomeResponsavel = nomeResponsavel;
        }

        public String getTelefoneResponsavel() {
            return telefoneResponsavel;
        }

        public void setTelefoneResponsavel(String telefoneResponsavel) {
            this.telefoneResponsavel = telefoneResponsavel;
        }

        public String getCpfResponsavel() {
            return cpfResponsavel;
        }

        public void setCpfResponsavel(String cpfResponsavel) {
            this.cpfResponsavel = cpfResponsavel;
        }

        public String getNomeCrianca() {
            return nomeCrianca;
        }

        public void setNomeCrianca(String nomeCrianca) {
            this.nomeCrianca = nomeCrianca;
        }

        public LocalDate getDataNascimento() {
            return dataNascimento;
        }

        public void setDataNascimento(LocalDate dataNascimento) {
            this.dataNascimento = dataNascimento;
        }

        public Long getOficinaId() {
            return oficinaId;
        }

        public void setOficinaId(Long oficinaId) {
            this.oficinaId = oficinaId;
        }
    }
}
