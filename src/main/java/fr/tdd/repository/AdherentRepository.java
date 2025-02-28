package fr.tdd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tdd.model.Adherent;

public interface AdherentRepository extends JpaRepository<Adherent, String> {


}
