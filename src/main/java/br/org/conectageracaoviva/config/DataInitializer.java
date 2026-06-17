package br.org.conectageracaoviva.config;

import br.org.conectageracaoviva.model.Perfil;
import br.org.conectageracaoviva.model.PerfilNome;
import br.org.conectageracaoviva.model.Oficina;
import br.org.conectageracaoviva.model.StatusVoluntario;
import br.org.conectageracaoviva.model.Usuario;
import br.org.conectageracaoviva.model.Voluntario;
import br.org.conectageracaoviva.repository.OficinaRepository;
import br.org.conectageracaoviva.repository.PerfilRepository;
import br.org.conectageracaoviva.repository.UsuarioRepository;
import br.org.conectageracaoviva.repository.VoluntarioRepository;
import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final String ADMIN_EMAIL = "arth210807@gmail.com";
    private static final String ADMIN_SENHA = "1616Vilela#";
    private static final String ADMIN_ANTIGO_EMAIL = "admin@geracaoviva.org";

    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final OficinaRepository oficinaRepository;
    private final VoluntarioRepository voluntarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PerfilRepository perfilRepository,
                           UsuarioRepository usuarioRepository,
                           OficinaRepository oficinaRepository,
                           VoluntarioRepository voluntarioRepository,
                           PasswordEncoder passwordEncoder) {
        this.perfilRepository = perfilRepository;
        this.usuarioRepository = usuarioRepository;
        this.oficinaRepository = oficinaRepository;
        this.voluntarioRepository = voluntarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Arrays.stream(PerfilNome.values()).forEach(this::criarPerfilSeNaoExistir);
        criarAdminInicialSeNaoExistir();
        criarUsuarioInicialSeNaoExistir(
                "Voluntario",
                "voluntario@geracaoviva.org",
                "voluntario123",
                PerfilNome.VOLUNTARIO
        );
        criarVoluntarioInicialSeNaoExistir();
        criarUsuarioInicialSeNaoExistir(
                "Comunidade",
                "comunidade@geracaoviva.org",
                "comunidade123",
                PerfilNome.COMUNIDADE
        );
        criarOficinasIniciaisSeNaoExistirem();
    }

    private void criarPerfilSeNaoExistir(PerfilNome nome) {
        if (perfilRepository.findByNome(nome).isEmpty()) {
            Perfil perfil = new Perfil();
            perfil.setNome(nome);
            perfilRepository.save(perfil);
        }
    }

    private void criarAdminInicialSeNaoExistir() {
        Perfil perfilAdmin = perfilRepository.findByNome(PerfilNome.ADMINISTRADOR)
                .orElseThrow(() -> new IllegalStateException("Perfil ADMINISTRADOR nao encontrado."));

        Usuario admin = usuarioRepository.findByEmail(ADMIN_EMAIL)
                .orElseGet(() -> {
                    Usuario novoAdmin = new Usuario();
                    novoAdmin.setNome("Administrador");
                    return novoAdmin;
                });
        admin.setEmail(ADMIN_EMAIL);
        admin.setSenha(passwordEncoder.encode(ADMIN_SENHA));
        admin.setAtivo(true);
        admin.getPerfis().add(perfilAdmin);

        usuarioRepository.save(admin);
        desativarAdminAntigo();
    }

    private void desativarAdminAntigo() {
        usuarioRepository.findByEmail(ADMIN_ANTIGO_EMAIL).ifPresent(usuario -> {
            usuario.setAtivo(false);
            usuarioRepository.save(usuario);
        });
    }

    private void criarUsuarioInicialSeNaoExistir(String nome, String email, String senha, PerfilNome perfilNome) {
        if (usuarioRepository.findByEmail(email).isPresent()) {
            return;
        }

        Perfil perfil = perfilRepository.findByNome(perfilNome)
                .orElseThrow(() -> new IllegalStateException("Perfil " + perfilNome + " nao encontrado."));

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.getPerfis().add(perfil);

        usuarioRepository.save(usuario);
    }

    private void criarVoluntarioInicialSeNaoExistir() {
        Usuario usuario = usuarioRepository.findByEmail("voluntario@geracaoviva.org")
                .orElseThrow(() -> new IllegalStateException("Usuario voluntario inicial nao encontrado."));

        if (voluntarioRepository.findByUsuarioEmail(usuario.getEmail()).isPresent()) {
            return;
        }

        Voluntario voluntario = new Voluntario();
        voluntario.setUsuario(usuario);
        voluntario.setTelefone("(11) 90000-0000");
        voluntario.setHabilidades("Apoio em oficinas, eventos e organizacao comunitaria.");
        voluntario.setStatus(StatusVoluntario.ATIVO);
        voluntarioRepository.save(voluntario);
    }

    private void criarOficinasIniciaisSeNaoExistirem() {
        if (oficinaRepository.count() > 0) {
            return;
        }

        criarOficina("Reforco Escolar", "Apoio em leitura, escrita e matematica.", 20, "Segundas e quartas - 14h", "Equipe pedagogica");
        criarOficina("Musica e Coral", "Expressao musical e convivencia em grupo.", 15, "Tercas - 15h", "Instrutor de musica");
        criarOficina("Esporte e Movimento", "Atividades fisicas, jogos cooperativos e disciplina.", 25, "Sextas - 9h", "Educador esportivo");
    }

    private void criarOficina(String nome, String descricao, int vagas, String horarios, String instrutor) {
        Oficina oficina = new Oficina();
        oficina.setNome(nome);
        oficina.setDescricao(descricao);
        oficina.setNumeroVagas(vagas);
        oficina.setHorarios(horarios);
        oficina.setInstrutorResponsavel(instrutor);
        oficinaRepository.save(oficina);
    }
}
