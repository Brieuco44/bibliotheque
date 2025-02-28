package fr.tdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tdd.exception.QuotaLivreDepasseException;
import fr.tdd.exception.AdherentNotFoundException;
import fr.tdd.exception.LivreIndisponibleException;
import fr.tdd.model.Adherent;
import fr.tdd.model.Civilite;
import fr.tdd.model.Format;
import fr.tdd.model.Livre;
import fr.tdd.model.Reservation;
import fr.tdd.repository.AdherentRepository;
import fr.tdd.repository.ReservationRepository;
import fr.tdd.service.ReservationService;

@ExtendWith(MockitoExtension.class)
public class ReservationTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private AdherentRepository adherentRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Livre livreExistant;
    private Adherent adherentExistant;
    private Reservation reservationExistant;

    @BeforeEach
    void setUp() {
        livreExistant = new Livre();
        livreExistant.setIsbn("2010008995");
        livreExistant.setTitre("TDD en Java");
        livreExistant.setAuteur("Martin Fowler");
        livreExistant.setEditeur("O'Reilly");
        livreExistant.setFormat(Format.Poche);
        livreExistant.setDisponible(true);

        adherentExistant = new Adherent();
        adherentExistant.setCode("123456");
        adherentExistant.setNom("Courapié");
        adherentExistant.setPrenom("Brieuc");
        adherentExistant.setDateNaissance(LocalDateTime.parse("1999-01-01T00:00:00"));
        adherentExistant.setCivilite(Civilite.Homme);

        reservationExistant = new Reservation();
        reservationExistant.setId("123456");
        reservationExistant.setLivre(livreExistant);
        reservationExistant.setAdherent(adherentExistant);
    }

    @Test
    void testCreerReservation() {
        // Given
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservationExistant);
        when(adherentRepository.existsById(reservationExistant.getAdherent().getCode())).thenReturn(true);
        when(reservationRepository.countByAdherentCodeAndDateFinIsNull(reservationExistant.getAdherent().getCode())).thenReturn(0);
        // When
        Reservation result = reservationService.creerReservation(reservationExistant);

        // Then
        assertNotNull(result);
        assertEquals(reservationExistant, result);
        verify( reservationRepository, times(1)).save(reservationExistant);
    }

    @Test
    void testCreerReservationLivreNonDisponible() {
        // Given
        livreExistant.setDisponible(false);
        
        // Then
        assertThrows(LivreIndisponibleException.class, () -> reservationService.creerReservation(reservationExistant));
        verify( reservationRepository, times(0)).save(reservationExistant);
    }

    @Test
    void testCreerReservationAdherentInconnu() {
        // Given
        when(adherentRepository.existsById(reservationExistant.getAdherent().getCode())).thenReturn(false);
        when(reservationRepository.countByAdherentCodeAndDateFinIsNull(reservationExistant.getAdherent().getCode())).thenReturn(1);
        reservationExistant.getAdherent().setCode("123456");
        
        // Then
        assertThrows(AdherentNotFoundException.class, () -> reservationService.creerReservation(reservationExistant));
        verify( reservationRepository, times(0)).save(reservationExistant);
    }

    @Test
    void testCreerReservationQuotaDepasse() {
        // Given
        when(reservationRepository.countByAdherentCodeAndDateFinIsNull(reservationExistant.getAdherent().getCode())).thenReturn(3);
        
        // Then
        assertThrows(QuotaLivreDepasseException.class, () -> reservationService.creerReservation(reservationExistant));
        verify( reservationRepository, times(0)).save(reservationExistant);
    }

    @Test
    void testTerminerReservation() {
        // Given
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservationExistant);
        when(reservationRepository.findById(reservationExistant.getId())).thenReturn(Optional.of(reservationExistant));
        
        // When
        Reservation result = reservationService.terminerReservation(reservationExistant.getId());

        // Then
        assertNotNull(result);
        assertEquals(reservationExistant, result);
        verify( reservationRepository, times(1)).save(reservationExistant);
    }

}
