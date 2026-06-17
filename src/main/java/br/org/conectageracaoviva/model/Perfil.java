package br.org.conectageracaoviva.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PerfilNome nome;

    public Long getId() {
        return id;
    }

    public PerfilNome getNome() {
        return nome;
    }

    public void setNome(PerfilNome nome) {
        this.nome = nome;
    }
}
