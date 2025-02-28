package fr.tdd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tdd.model.Livre;

public interface LivreRepository extends JpaRepository<Livre, String> {
    public Livre findByIsbn(String isbn);
    public List<Livre> findByTitre(String titre);
    public List<Livre> findByAuteur(String auteur);
}
