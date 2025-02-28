package fr.tdd.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import fr.tdd.exception.AdherentNotFoundException;
import fr.tdd.exception.LivreIndisponibleException;
import fr.tdd.exception.QuotaLivreDepasseException;
import fr.tdd.model.Reservation;
import fr.tdd.repository.AdherentRepository;
import fr.tdd.repository.ReservationRepository;

@Service
public class ReservationService {
    public final static int QUOTA_LIVRE = 3;
    public final static int DUREE_MAX_MOIS = 4;
    public final ReservationRepository reservationRepository;
    public final AdherentRepository adherentRepository;

    public ReservationService(ReservationRepository reservationRepository, AdherentRepository adherentRepository) {
        this.reservationRepository = reservationRepository;
        this.adherentRepository = adherentRepository;
    }

    public Reservation creerReservation(Reservation reservation){
        if (this.isQuotaDepasse(reservation.getAdherent().getCode())) {
            throw new QuotaLivreDepasseException("Le quota de réservations est dépassé.");
        }
        if (reservation.getLivre().isDisponible() == false) {
            throw new LivreIndisponibleException("Le livre n'est pas disponible.");
        }
        if (!adherentRepository.existsById(reservation.getAdherent().getCode())) {
            throw new AdherentNotFoundException("L'adhérent n'existe pas.");
        }
        return reservationRepository.save(reservation);
    }

    public Reservation terminerReservation(String id) {
        Reservation reservation = reservationRepository.findById(id).get();
        reservation.setDateFin(LocalDateTime.now());
        reservationRepository.save(reservation);
        return reservation;
    }

    public Boolean isQuotaDepasse(String codeAdherent) {
        return reservationRepository.countByAdherentCodeAndDateFinIsNull(codeAdherent) >= QUOTA_LIVRE;
    }
}
