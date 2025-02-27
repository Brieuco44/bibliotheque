package fr.tdd;

import fr.tdd.model.Livre;
import fr.tdd.repository.LivreRepository;
import fr.tdd.service.LivreService;
import fr.tdd.exception.LivreNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LivreTest {

    @Mock
    private LivreRepository livreRepository;

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