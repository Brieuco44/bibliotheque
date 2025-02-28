package fr.tdd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.tdd.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
    public int countByAdherentCodeAndDateFinIsNull(String codeAdherent);

}
