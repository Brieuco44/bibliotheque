package fr.tdd;

import fr.tdd.model.Format;
import fr.tdd.model.Livre;
import fr.tdd.repository.LivreRepository;
import fr.tdd.service.LivreService;
import fr.tdd.service.WebServiceLivre;
import fr.tdd.exception.IsbnInvalideException;
import fr.tdd.exception.LivreDejaExistantException;
import fr.tdd.exception.LivreNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LivreTest {

    @Mock
    private LivreRepository livreRepository;

    @Mock
    private WebServiceLivre webServiceLivre;

    @InjectMocks
    private LivreService livreService;

    private Livre livre;

    @BeforeEach
    void setUp() {
        livre = new Livre();
        livre.setIsbn("123456");
        livre.setTitre("TDD en Java");
        livre.setAuteur("Martin Fowler");
        livre.setEditeur("O'Reilly");
        livre.setFormat(Format.Poche);
        livre.setDisponible(true);
    }

    @Test
    void testCreerLivre() {
        // Given
        when(livreRepository.save(livre)).thenReturn(livre);
        
        // When
        Livre result = livreService.creerLivre(livre);

        // Then
        assertNotNull(result);
        assertEquals("123456", result.getIsbn());
        verify(livreRepository, times(1)).save(livre);
    }

    @Test
    void testCreerLivre_IsbnInvalide() {
        livre.setIsbn("123"); // ISBN invalide

        assertThrows(IsbnInvalideException.class, () -> livreService.creerLivre(livre));
        verify(livreRepository, never()).save(any());
    }

    @Test
    void testCreerLivre_DejaExistant() {
        when(livreRepository.existsById(livre.getIsbn())).thenReturn(true);

        assertThrows(LivreDejaExistantException.class, () -> livreService.creerLivre(livre));
        verify(livreRepository, never()).save(any());
    }

    @Test
    void testCreerLivre_InformationsManquantesRecuperees() {
        livre.setTitre(null); // Information manquante

        Livre livreComplet = new Livre();
        livreComplet.setIsbn("9783161484100");
        livreComplet.setTitre("TDD en Java");
        livreComplet.setAuteur("Martin Fowler");
        livreComplet.setEditeur("O'Reilly");
        livreComplet.setDisponible(true);

        when(webServiceLivre.recupererInformations("9783161484100")).thenReturn(livreComplet);
        when(livreRepository.save(any())).thenReturn(livreComplet);

        Livre result = livreService.creerLivre(livre);

        assertNotNull(result);
        assertEquals("TDD en Java", result.getTitre());
        verify(webServiceLivre, times(1)).recupererInformations("9783161484100");
        verify(livreRepository, times(1)).save(livreComplet);
    }

    @Test
    void testObtenirLivre_Existe() {
        // Given
        when(livreRepository.findById("123456")).thenReturn(Optional.of(livre));

        // When
        Livre result = livreService.obtenirLivre("123456");

        // Then
        assertNotNull(result);
        assertEquals("TDD en Java", result.getTitre());
    }

    @Test
    void testObtenirLivre_NonExistant() {
        when(livreRepository.findById("999999")).thenReturn(Optional.empty());
        assertThrows(LivreNotFoundException.class, () -> livreService.obtenirLivre("999999"));
    }

    @Test
    void testMettreAJourLivre() {
        when(livreRepository.findById("123456")).thenReturn(Optional.of(livre));
        when(livreRepository.save(any(Livre.class))).thenReturn(livre);

        Livre livreMiseAJour = new Livre();
        livreMiseAJour.setIsbn("123456");
        livreMiseAJour.setTitre("TDD en Java - 2ème Édition");

        Livre result = livreService.mettreAJourLivre("123456", livreMiseAJour);
        assertEquals("TDD en Java - 2ème Édition", result.getTitre());
    }

    @Test
    void testSupprimerLivre_Existe() {
        when(livreRepository.findById("123456")).thenReturn(Optional.of(livre));
        doNothing().when(livreRepository).delete(livre);

        assertDoesNotThrow(() -> livreService.supprimerLivre("123456"));
        verify(livreRepository, times(1)).delete(livre);
    }

    @Test
    void testSupprimerLivre_NonExistant() {
        when(livreRepository.findById("999999")).thenReturn(Optional.empty());
        assertThrows(LivreNotFoundException.class, () -> livreService.supprimerLivre("999999"));
    }
}