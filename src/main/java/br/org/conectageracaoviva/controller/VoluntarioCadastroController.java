package br.org.conectageracaoviva.controller;

import br.org.conectageracaoviva.model.Perfil;
import br.org.conectageracaoviva.model.PerfilNome;
import br.org.conectageracaoviva.model.StatusVoluntario;
import br.org.conectageracaoviva.model.Usuario;
import br.org.conectageracaoviva.model.Voluntario;
import br.org.conectageracaoviva.repository.PerfilRepository;
import br.org.conectageracaoviva.repository.UsuarioRepository;
import br.org.conectageracaoviva.repository.VoluntarioRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class VoluntarioCadastroController {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final VoluntarioRepository voluntarioRepository;
    private final PasswordEncoder passwordEncoder;

    public VoluntarioCadastroController(UsuarioRepository usuarioRepository,
                                        PerfilRepository perfilRepository,
                                        VoluntarioRepository voluntarioRepository,
                                        PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.voluntarioRepository = voluntarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/voluntarios/novo")
    public String novoVoluntario(Model model) {
        model.addAttribute("voluntarioForm", new VoluntarioForm());
        return "voluntario-form";
    }

    @PostMapping("/voluntarios/novo")
    public String cadastrarVoluntario(@Valid @ModelAttribute("voluntarioForm") VoluntarioForm form,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "voluntario-form";
        }

        Perfil perfilVoluntario = perfilRepository.findByNome(PerfilNome.VOLUNTARIO)
                .orElseThrow(() -> new IllegalStateException("Perfil VOLUNTARIO nao encontrado."));

        Usuario usuario = usuarioRepository.findByEmail(form.getEmail())
                .orElseGet(() -> criarUsuario(form));

        if (!passwordEncoder.matches(form.getSenha(), usuario.getSenha())) {
            bindingResult.rejectValue("senha", "senha.incorreta", "Este email ja existe. Informe a senha correta para vincular o perfil de voluntario.");
            return "voluntario-form";
        }

        usuario.setNome(form.getNome());
        usuario.getPerfis().add(perfilVoluntario);
        usuarioRepository.save(usuario);

        Voluntario voluntario = voluntarioRepository.findByUsuarioEmail(usuario.getEmail())
                .orElseGet(Voluntario::new);

        if (voluntario.getUsuario() == null) {
            voluntario.setUsuario(usuario);
            voluntario.setStatus(StatusVoluntario.PENDENTE);
        }

        voluntario.setTelefone(form.getTelefone());
        voluntario.setHabilidades(form.getHabilidades());
        voluntarioRepository.save(voluntario);

        redirectAttributes.addFlashAttribute("voluntarioSucesso", "Perfil de voluntario vinculado. Entre usando este mesmo email e senha.");
        return "redirect:/login?perfil=voluntario";
    }

    private Usuario criarUsuario(VoluntarioForm form) {
        Usuario usuario = new Usuario();
        usuario.setNome(form.getNome());
        usuario.setEmail(form.getEmail());
        usuario.setSenha(passwordEncoder.encode(form.getSenha()));
        return usuario;
    }

    public static class VoluntarioForm {

        @NotBlank(message = "Informe seu nome.")
        private String nome;

        @Email(message = "Informe um email valido.")
        @NotBlank(message = "Informe seu email.")
        private String email;

        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
        @NotBlank(message = "Informe uma senha.")
        private String senha;

        @NotBlank(message = "Informe seu telefone.")
        private String telefone;

        private String disponibilidade;

        private String habilidades;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }

        public String getTelefone() {
            return telefone;
        }

        public void setTelefone(String telefone) {
            this.telefone = telefone;
        }

        public String getDisponibilidade() {
            return disponibilidade;
        }

        public void setDisponibilidade(String disponibilidade) {
            this.disponibilidade = disponibilidade;
        }

        public String getHabilidades() {
            return habilidades;
        }

        public void setHabilidades(String habilidades) {
            this.habilidades = habilidades;
        }
    }
}
