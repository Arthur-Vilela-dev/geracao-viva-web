package br.org.conectageracaoviva.security;

import br.org.conectageracaoviva.model.Perfil;
import br.org.conectageracaoviva.model.Usuario;
import br.org.conectageracaoviva.repository.UsuarioRepository;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario nao encontrado."));

        List<SimpleGrantedAuthority> permissoes = usuario.getPerfis()
                .stream()
                .map(Perfil::getNome)
                .map(nome -> new SimpleGrantedAuthority("ROLE_" + nome.name()))
                .toList();

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities(permissoes)
                .disabled(!usuario.isAtivo())
                .build();
    }
}
