package fr.tdd;

import fr.tdd.model.Format;
import fr.tdd.model.Livre;
import fr.tdd.repository.LivreRepository;
import fr.tdd.service.LivreService;
import fr.tdd.service.WebServiceLivre;
import fr.tdd.exception.IsbnInvalideException;
import fr.tdd.exception.LivreDejaExistantException;
import fr.tdd.exception.LivreNotFoundException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
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

    private Livre livreExistant;

    @BeforeEach
    void setUp() {
        livreExistant = new Livre();
        livreExistant.setIsbn("2010008995");
        livreExistant.setTitre("TDD en Java");
        livreExistant.setAuteur("Martin Fowler");
        livreExistant.setEditeur("O'Reilly");
        livreExistant.setFormat(Format.Poche);
        livreExistant.setDisponible(true);
    }

    @Test
    void testCreerLivre() {
        // Given
        when(livreRepository.save(livreExistant)).thenReturn(livreExistant);
        
        // When
        Livre result = livreService.creerLivre(livreExistant);

        // Then
        assertNotNull(result);
        assertEquals("2010008995", result.getIsbn());
        verify(livreRepository, times(1)).save(livreExistant);
    }

    @Test
    void testCreerLivre_IsbnInvalide() {
        livreExistant.setIsbn("123"); // ISBN invalide

        assertThrows(IsbnInvalideException.class, () -> livreService.creerLivre(livreExistant));
        verify(livreRepository, never()).save(any());
    }

    @Test
    void testCreerLivre_DejaExistant() {
        when(livreRepository.existsById(livreExistant.getIsbn())).thenReturn(true);

        assertThrows(LivreDejaExistantException.class, () -> livreService.creerLivre(livreExistant));
        verify(livreRepository, never()).save(any());
    }

    @Test
    void testCreerLivre_InformationsManquantesRecuperees() {
        livreExistant.setTitre(null); // Information manquante

        Livre livreComplet = new Livre();
        livreComplet.setIsbn("2010008995");
        livreComplet.setTitre("TDD en Java");
        livreComplet.setAuteur("Martin Fowler");
        livreComplet.setEditeur("O'Reilly");
        livreComplet.setDisponible(true);

        when(webServiceLivre.recupererInformations("2010008995")).thenReturn(livreComplet);
        when(livreRepository.save(any())).thenReturn(livreComplet);

        Livre result = livreService.creerLivre(livreExistant);

        assertNotNull(result);
        assertEquals("TDD en Java", result.getTitre());
        verify(webServiceLivre, times(1)).recupererInformations("2010008995");
        verify(livreRepository, times(1)).save(livreComplet);
    }

    @Test
    void testObtenirLivre_Existe() {
        // Given
        when(livreRepository.findById("2210765528")).thenReturn(Optional.of(livreExistant));

        // When
        Livre result = livreService.obtenirLivre("2210765528");

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
        when(livreRepository.save(any(Livre.class))).thenReturn(livreExistant);
        when(livreRepository.existsById("2010008995")).thenReturn(true);
        when(livreRepository.findById("2010008995")).thenReturn(Optional.of(livreExistant));

        Livre livreMiseAJour = new Livre();
        livreMiseAJour.setIsbn("2010008995");
        livreMiseAJour.setTitre("TDD en Java - 2ème Édition");

        Livre result = livreService.mettreAJourLivre("2010008995", livreMiseAJour);
        assertEquals("TDD en Java - 2ème Édition", result.getTitre());
    }

    @Test
    void testSupprimerLivre_Existe() {
        when(livreRepository.findById("2010008995")).thenReturn(Optional.of(livreExistant));
        doNothing().when(livreRepository).delete(livreExistant);

        assertDoesNotThrow(() -> livreService.supprimerLivre("2010008995"));
        verify(livreRepository, times(1)).delete(livreExistant);
    }

    @Test
    void testSupprimerLivre_NonExistant() {
        when(livreRepository.findById("999999")).thenReturn(Optional.empty());
        assertThrows(LivreNotFoundException.class, () -> livreService.supprimerLivre("999999"));
    }

    @Test
    void testRechercherParIsbn_Existe() {
        when(livreRepository.findByIsbn("2010008995")).thenReturn(Optional.of(livreExistant));

        Livre result = livreService.rechercherParIsbn("2010008995");

        assertNotNull(result);
        assertEquals("2010008995", result.getIsbn());
        verify(livreRepository, times(1)).findByIsbn("2010008995");
    }

    @Test
    void testRechercherParIsbn_NonExistant() {
        when(livreRepository.findByIsbn("999999")).thenReturn(Optional.empty());

        assertThrows(LivreNotFoundException.class, () -> livreService.rechercherParIsbn("999999"));
        verify(livreRepository, times(1)).findByIsbn("999999");
    }

    @Test
    void testRechercherParAuteur_Existe() {
        when(livreRepository.findByAuteur("Martin Fowler")).thenReturn(List.of(livreExistant));

        List<Livre> result = livreService.rechercherParAuteur("Martin Fowler");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Martin Fowler", result.get(0).getAuteur());
        verify(livreRepository, times(1)).findByAuteur("Martin Fowler");
    }

    @Test
    void testRechercherParAuteur_NonExistant() {
        when(livreRepository.findByAuteur("Unknown Author")).thenReturn(Collections.emptyList());

        List<Livre> result = livreService.rechercherParAuteur("Unknown Author");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(livreRepository, times(1)).findByAuteur("Unknown Author");
    }

    @Test
    void testRechercherParTitre_Existe() {
        when(livreRepository.findByTitre("TDD en Java")).thenReturn(List.of(livreExistant));

        List<Livre> result = livreService.rechercherParTitre("TDD en Java");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("TDD en Java", result.get(0).getTitre());
        verify(livreRepository, times(1)).findByTitre("TDD en Java");
    }

    @Test
    void testRechercherParTitre_NonExistant() {
        when(livreRepository.findByTitre("Unknown Title")).thenReturn(Collections.emptyList());

        List<Livre> result = livreService.rechercherParTitre("Unknown Title");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(livreRepository, times(1)).findByTitre("Unknown Title");
    }
}