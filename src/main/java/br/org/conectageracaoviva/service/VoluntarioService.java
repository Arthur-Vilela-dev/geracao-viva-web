package br.org.conectageracaoviva.service;

import br.org.conectageracaoviva.model.StatusVoluntario;
import br.org.conectageracaoviva.model.Usuario;
import br.org.conectageracaoviva.model.Voluntario;
import br.org.conectageracaoviva.repository.UsuarioRepository;
import br.org.conectageracaoviva.repository.VoluntarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class VoluntarioService {

    private final VoluntarioRepository voluntarioRepository;
    private final UsuarioRepository usuarioRepository;

    public VoluntarioService(VoluntarioRepository voluntarioRepository,
                             UsuarioRepository usuarioRepository) {
        this.voluntarioRepository = voluntarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Voluntario> listarTodos() {
        return voluntarioRepository.findAll();
    }

    public Voluntario alterarStatus(Long id, StatusVoluntario status) {
        Voluntario voluntario = voluntarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Voluntario nao encontrado."));
        voluntario.setStatus(status);
        return voluntarioRepository.save(voluntario);
    }

    public Voluntario buscarPorEmail(String email) {
        return voluntarioRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Voluntario nao encontrado para este usuario."));
    }

    public Voluntario atualizarDados(String email, String nome, String telefone, String habilidades) {
        Voluntario voluntario = buscarPorEmail(email);
        Usuario usuario = voluntario.getUsuario();
        usuario.setNome(nome);
        usuarioRepository.save(usuario);

        voluntario.setTelefone(telefone);
        voluntario.setHabilidades(habilidades);
        return voluntarioRepository.save(voluntario);
    }

    public void excluir(Long id) {
        if (!voluntarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Voluntario nao encontrado.");
        }

        voluntarioRepository.deleteById(id);
    }
}
