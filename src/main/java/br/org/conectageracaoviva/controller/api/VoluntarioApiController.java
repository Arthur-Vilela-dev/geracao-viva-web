package br.org.conectageracaoviva.controller.api;

import br.org.conectageracaoviva.model.StatusVoluntario;
import br.org.conectageracaoviva.model.Voluntario;
import br.org.conectageracaoviva.service.VoluntarioService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/voluntarios")
public class VoluntarioApiController {

    private final VoluntarioService voluntarioService;

    public VoluntarioApiController(VoluntarioService voluntarioService) {
        this.voluntarioService = voluntarioService;
    }

    @GetMapping
    public List<VoluntarioResponse> listar() {
        return voluntarioService.listarTodos()
                .stream()
                .map(VoluntarioResponse::from)
                .toList();
    }

    public record VoluntarioResponse(
            Long id,
            String nome,
            String email,
            String telefone,
            String habilidades,
            StatusVoluntario status
    ) {
        public static VoluntarioResponse from(Voluntario voluntario) {
            return new VoluntarioResponse(
                    voluntario.getId(),
                    voluntario.getUsuario().getNome(),
                    voluntario.getUsuario().getEmail(),
                    voluntario.getTelefone(),
                    voluntario.getHabilidades(),
                    voluntario.getStatus()
            );
        }
    }
}
