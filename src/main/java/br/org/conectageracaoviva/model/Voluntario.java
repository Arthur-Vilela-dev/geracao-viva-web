package br.org.conectageracaoviva.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Voluntario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento 1:1: cada voluntario esta ligado a um usuario de login.
    @OneToOne
    private Usuario usuario;

    @NotBlank
    private String telefone;

    private String habilidades;

    @Enumerated(EnumType.STRING)
    private StatusVoluntario status = StatusVoluntario.PENDENTE;

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public StatusVoluntario getStatus() {
        return status;
    }

    public void setStatus(StatusVoluntario status) {
        this.status = status;
    }
}
