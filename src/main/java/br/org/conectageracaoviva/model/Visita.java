package br.org.conectageracaoviva.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Visita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nomeVisitante;

    @Email
    private String emailVisitante;

    @NotBlank
    private String telefoneVisitante;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime dataHorario;

    @Enumerated(EnumType.STRING)
    private StatusVisita status = StatusVisita.AGENDADA;

    public Long getId() {
        return id;
    }

    public String getNomeVisitante() {
        return nomeVisitante;
    }

    public void setNomeVisitante(String nomeVisitante) {
        this.nomeVisitante = nomeVisitante;
    }

    public String getEmailVisitante() {
        return emailVisitante;
    }

    public void setEmailVisitante(String emailVisitante) {
        this.emailVisitante = emailVisitante;
    }

    public String getTelefoneVisitante() {
        return telefoneVisitante;
    }

    public void setTelefoneVisitante(String telefoneVisitante) {
        this.telefoneVisitante = telefoneVisitante;
    }

    public LocalDateTime getDataHorario() {
        return dataHorario;
    }

    public void setDataHorario(LocalDateTime dataHorario) {
        this.dataHorario = dataHorario;
    }

    public StatusVisita getStatus() {
        return status;
    }

    public void setStatus(StatusVisita status) {
        this.status = status;
    }
}
