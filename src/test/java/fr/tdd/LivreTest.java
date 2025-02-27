package fr.tdd;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import fr.tdd.model.Livre;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;

import fr.tdd.api.LivreAPI;
import fr.tdd.model.Format;

public class LivreTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private LivreAPI livreApi;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateLivre() {
        Livre livre = new Livre();
        livre.setIsbn("123456789");
        livre.setTitre("Test Title");
        livre.setAuteur("Test Author");
        livre.setEditeur("Test Publisher");
        livre.setFormat(Format.Poche);
        livre.setDisponible(true);

        livreApi.createLivre(livre);

        verify(entityManager, times(1)).persist(livre);
    }

    @Test
    public void testReadLivre() {
        String isbn = "123456789";
        Livre livre = new Livre();
        livre.setIsbn(isbn);

        when(entityManager.find(Livre.class, isbn)).thenReturn(livre);

        Livre foundLivre = livreApi.readLivre(isbn);

        verify(entityManager, times(1)).find(Livre.class, isbn);
        assertEquals(livre, foundLivre);
    }

    @Test
    public void testUpdateLivre() {
        Livre livre = new Livre();
        livre.setIsbn("123456789");
        livre.setTitre("Updated Title");

        livreApi.updateLivre(livre);

        verify(entityManager, times(1)).merge(livre);
    }

    @Test
    public void testDeleteLivre() {
        String isbn = "123456789";
        Livre livre = new Livre();
        livre.setIsbn(isbn);

        when(entityManager.find(Livre.class, isbn)).thenReturn(livre);

        livreApi.deleteLivre(isbn);

        verify(entityManager, times(1)).remove(livre);
    }

    @Test
    public void testCreateLivreWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            livreApi.createLivre(null);
        });
    }

    @Test
    public void testReadLivreNotFound() {
        String isbn = "123456789";

        when(entityManager.find(Livre.class, isbn)).thenReturn(null);

        Livre foundLivre = livreApi.readLivre(isbn);

        verify(entityManager, times(1)).find(Livre.class, isbn);
        assertNull(foundLivre);
    }

    @Test
    public void testUpdateLivreWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            livreApi.updateLivre(null);
        });
    }

    @Test
    public void testDeleteLivreNotFound() {
        String isbn = "123456789";

        when(entityManager.find(Livre.class, isbn)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            livreApi.deleteLivre(isbn);
        });

        verify(entityManager, times(1)).find(Livre.class, isbn);
        verify(entityManager, times(0)).remove(any(Livre.class));
    }
}