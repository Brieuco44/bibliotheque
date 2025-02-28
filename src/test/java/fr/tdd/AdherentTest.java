package fr.tdd;

import java.sql.Date;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.tdd.exception.AdherentNotFoundException;
import fr.tdd.model.Adherent;
import fr.tdd.model.Civilite;
import fr.tdd.model.Livre;
import fr.tdd.repository.AdherentRepository;
import fr.tdd.repository.LivreRepository;
import fr.tdd.service.AdherentService;
import fr.tdd.service.LivreService;
import fr.tdd.service.WebServiceLivre;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.Test;

@ExtendWith(MockitoExtension.class)
public class AdherentTest {
    @Mock
    private AdherentRepository adherentRepository;

    @InjectMocks
    private AdherentService adherentService;

    private Adherent adherentExistant;

    @BeforeEach
    void setUp() {
        adherentExistant = new Adherent();
        adherentExistant.setCode("123456");
        adherentExistant.setNom("Courapié");
        adherentExistant.setPrenom("Brieuc");
        adherentExistant.setDateNaissance(LocalDateTime.parse("1999-01-01T00:00:00"));
        adherentExistant.setCivilite(Civilite.Homme);
    }

    @Test
    void testCreateAdherent() {
        when(adherentRepository.save(any(Adherent.class))).thenReturn(adherentExistant);

        Adherent createdAdherent = adherentService.createAdherent(adherentExistant);

        assertNotNull(createdAdherent);
        assertEquals("Courapié", createdAdherent.getNom());
        assertEquals("Brieuc", createdAdherent.getPrenom());
        verify(adherentRepository, times(1)).save(adherentExistant);
    }

    @Test
    void testCreateAdherentWithNullCode() {
        adherentExistant.setCode(null);

        assertThrows(AdherentNotFoundException.class, () -> adherentService.createAdherent(adherentExistant));
        verify(adherentRepository, times(0)).save(adherentExistant);

    }

    @Test
    void testReadAdherent() {
        when(adherentRepository.findById(any(String.class))).thenReturn(Optional.of(adherentExistant));

        Adherent foundAdherent = adherentService.getAdherentById("some-id");

        assertNotNull(foundAdherent);
        assertEquals("Courapié", foundAdherent.getNom());
        assertEquals("Brieuc", foundAdherent.getPrenom());
        verify(adherentRepository, times(1)).findById("some-id");
    }

    @Test
    void testReadAdherentNotFound() {
        when(adherentRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(AdherentNotFoundException.class, () -> adherentService.getAdherentById("non-existent-id"));
        verify(adherentRepository, times(1)).findById("non-existent-id");
    }

    @Test
    void testUpdateAdherent() {
        when(adherentRepository.findById(any(String.class))).thenReturn(Optional.of(adherentExistant));
        when(adherentRepository.save(any(Adherent.class))).thenReturn(adherentExistant);

        adherentExistant.setNom("UpdatedName");
        Adherent updatedAdherent = adherentService.updateAdherent("some-id", adherentExistant);

        assertNotNull(updatedAdherent);
        assertEquals("UpdatedName", updatedAdherent.getNom());
        verify(adherentRepository, times(1)).findById("some-id");
        verify(adherentRepository, times(1)).save(adherentExistant);
    }

    @Test
    void testUpdateAdherentNotFound() {
        when(adherentRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(AdherentNotFoundException.class, () -> adherentService.updateAdherent("non-existent-id", adherentExistant));
        verify(adherentRepository, times(1)).findById("non-existent-id");
    }

    @Test
    void testDeleteAdherent() {
        when(adherentRepository.findById(any(String.class))).thenReturn(Optional.of(adherentExistant));

        adherentService.deleteAdherent("123456");

        verify(adherentRepository, times(1)).findById("some-id");
        verify(adherentRepository, times(1)).delete(adherentExistant);
    }


    @Test
    void testDeleteAdherentNotFound() {
        when(adherentRepository.findById(any(String.class))).thenReturn(Optional.empty());

        assertThrows(AdherentNotFoundException.class, () -> adherentService.deleteAdherent("12345"));
        verify(adherentRepository, times(1)).findById("non-existent-id");
    }
}
