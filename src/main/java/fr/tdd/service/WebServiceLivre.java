package fr.tdd.service;

import fr.tdd.model.Format;
import fr.tdd.model.Livre;
import org.springframework.stereotype.Service;

@Service
public class WebServiceLivre {
    public Livre recupererInformations(String isbn) {
        // Simulation de récupération des données
        Livre livre = new Livre();
        livre.setIsbn(isbn);
        livre.setTitre("Titre depuis WS");
        livre.setAuteur("Auteur depuis WS");
        livre.setEditeur("Editeur depuis WS");
        livre.setFormat(Format.Broche);
        livre.setDisponible(true);
        return livre;
    }
}

