package fr.tdd.model;

import java.sql.Date;
import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "adherent")
@Getter
@Setter
public class Adherent {

    @Id
    @UuidGenerator
    private String code;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "date_naissance")
    private LocalDateTime dateNaissance;

    @Column(name = "civilite")
    private Civilite civilite;

}
