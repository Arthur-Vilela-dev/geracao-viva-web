package br.org.conectageracaoviva.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Esta tabela transforma o relacionamento N:N entre Crianca e Oficina em uma entidade.
    @ManyToOne
    private Crianca crianca;

    @ManyToOne
    private Oficina oficina;

    private LocalDate dataMatricula = LocalDate.now();

    public Long getId() {
        return id;
    }

    public Crianca getCrianca() {
        return crianca;
    }

    public void setCrianca(Crianca crianca) {
        this.crianca = crianca;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public LocalDate getDataMatricula() {
        return dataMatricula;
    }
}
