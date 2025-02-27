package fr.tdd.service;

import org.springframework.stereotype.Service;

import fr.tdd.exception.LivreNotFoundException;
import fr.tdd.model.Livre;
import fr.tdd.repository.LivreRepository;

@Service
public class LivreService {


    private final LivreRepository livreRepository;

    public LivreService(LivreRepository livreRepository) {
        this.livreRepository = livreRepository;
    }

    public Livre creerLivre(Livre livre) {
        return livreRepository.save(livre);
    }

    public Livre obtenirLivre(String isbn) {
        return livreRepository.findById(isbn)
                .orElseThrow(() -> new LivreNotFoundException());
    }

    public Livre mettreAJourLivre(String isbn, Livre livreDetails) {
        Livre livre = obtenirLivre(isbn);
        livre.setTitre(livreDetails.getTitre());
        livre.setAuteur(livreDetails.getAuteur());
        livre.setEditeur(livreDetails.getEditeur());
        livre.setDisponible(livreDetails.isDisponible());
        return livreRepository.save(livre);
    }

    public void supprimerLivre(String isbn) {
        Livre livre = obtenirLivre(isbn);
        livreRepository.delete(livre);
    }

}
