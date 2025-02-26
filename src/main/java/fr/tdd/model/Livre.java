package fr.tdd.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table( name = "livre" )
@Getter
@Setter
public class Livre {
    @Id
    private String isbn;

    @Column(name = "titre")
    private String titre;

    @Column(name = "auteur")
    private String auteur;

    @Column(name = "editeur")
    private String editeur;

    @Column(name = "format")
    private Format format;

    @Column(name = "disponible")
    private boolean disponible;

}
