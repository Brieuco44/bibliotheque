package fr.tdd.service;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tdd.ISBNValidator;
import fr.tdd.exception.IsbnInvalideException;
import fr.tdd.exception.LivreDejaExistantException;
import fr.tdd.exception.LivreNotFoundException;
import fr.tdd.model.Livre;
import fr.tdd.repository.LivreRepository;

@Service
public class LivreService {

    private final LivreRepository livreRepository;
    private final WebServiceLivre webServiceLivre;

    public LivreService(LivreRepository livreRepository, WebServiceLivre webServiceLivre) {
        this.livreRepository = livreRepository;
        this.webServiceLivre = webServiceLivre;
    }

    public Livre creerLivre(Livre livre) {
        // Vérifier que l'ISBN est valide
        ISBNValidator validator = new ISBNValidator();
        validator.validateISBN(livre.getIsbn());
        // Vérifier que le livre n'existe pas déjà
        if (livreRepository.existsById(livre.getIsbn())) {
            throw new LivreDejaExistantException("Un livre avec cet ISBN existe déjà.");
        }

        // Récupérer les informations manquantes
        if (informationsManquantes(livre)) {
            Livre livreComplet = webServiceLivre.recupererInformations(livre.getIsbn());
            if (livreComplet == null) {
                throw new LivreNotFoundException("Impossible de récupérer les informations du livre.");
            }
            return livreRepository.save(livreComplet);
        }

        return livreRepository.save(livre);
    }

    public Livre obtenirLivre(String isbn) {
        return livreRepository.findById(isbn)
                .orElseThrow(() -> new LivreNotFoundException("Livre non trouvé avec l'ISBN : " + isbn));
    }

    public Livre mettreAJourLivre(String isbn, Livre livreDetails) {

        // Vérifier que l'ISBN est valide
        ISBNValidator validator = new ISBNValidator();
        validator.validateISBN(isbn);

        // Vérifier que le livre existe
        if (!livreRepository.existsById(isbn)) {
            throw new LivreNotFoundException("Ce livre n'existe pas.");
        }

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

    private boolean informationsManquantes(Livre livre) {
        return livre.getTitre() == null || livre.getAuteur() == null || livre.getEditeur() == null;
    }

    public Livre rechercherParIsbn(String isbn) {
        try {
            return livreRepository.findByIsbn(isbn);
        } catch (Exception e) {
            throw new LivreNotFoundException("Ce livre n'existe pas.");
        }
    }

    public List<Livre> rechercherParTitre(String titre) {
        try {
            return livreRepository.findByTitre(titre);
        } catch (LivreNotFoundException e) {
            throw new LivreNotFoundException("Ce livre n'existe pas.");
        }
    }

    public List<Livre> rechercherParAuteur(String auteur) {
        try {
            return livreRepository.findByAuteur(auteur);
        } catch (LivreNotFoundException e) {
            throw new LivreNotFoundException("Ce livre n'existe pas.");
        }
    }

}
