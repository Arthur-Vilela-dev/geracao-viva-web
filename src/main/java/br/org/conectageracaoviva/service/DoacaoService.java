package br.org.conectageracaoviva.service;

import br.org.conectageracaoviva.model.Doacao;
import br.org.conectageracaoviva.model.Usuario;
import br.org.conectageracaoviva.repository.DoacaoRepository;
import br.org.conectageracaoviva.repository.UsuarioRepository;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class DoacaoService {

    private final DoacaoRepository doacaoRepository;
    private final UsuarioRepository usuarioRepository;

    public DoacaoService(DoacaoRepository doacaoRepository,
                         UsuarioRepository usuarioRepository) {
        this.doacaoRepository = doacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Doacao> listarTodas() {
        return doacaoRepository.findAll();
    }

    public Doacao registrar(Doacao doacao, Authentication authentication) {
        // Se a pessoa estiver logada, gravamos quem fez a doacao.
        if (authentication != null && authentication.isAuthenticated()) {
            usuarioRepository.findByEmail(authentication.getName())
                    .ifPresent(doacao::setDoador);
        }

        return doacaoRepository.save(doacao);
    }
}
