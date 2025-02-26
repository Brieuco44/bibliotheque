package fr.tdd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tdd.model.Livre;

public interface LivreRepository extends JpaRepository<Livre, String> {

}
