package fr.tdd;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import fr.tdd.exception.IsbnInvalideException;

public class ISBNValidatorTest {

    @Test
    void checkValid10CharsISBNCode() {
        ISBNValidator validator = new ISBNValidator();
        boolean result = validator.validateISBN("2210765528");
        assertTrue(result, "first assertion");
        result = validator.validateISBN("2226392122");
        assertTrue(result, "second assertion");
    }

    @Test
    void checkInvalid10CharsISBNCode() {
        ISBNValidator validator = new ISBNValidator();
        boolean result = validator.validateISBN("2210765525");
        assertFalse(result);
    }

    @Test
    void invalidLengthShouldThrowsException() {
        ISBNValidator validator = new ISBNValidator();
        assertThrows(IsbnInvalideException.class, () -> validator.validateISBN("123456789"));
        assertThrows(IsbnInvalideException.class, () -> validator.validateISBN("12345678912"));
    }

    @Test
    void nonNumericISBNThrowsException() {
        ISBNValidator validator = new ISBNValidator();
        assertThrows(IsbnInvalideException.class, () -> validator.validateISBN("helloworld"));
    }

    @Test
    void checkISBNEndingWithAnXIsValid() {
        ISBNValidator validator = new ISBNValidator();
        boolean result = validator.validateISBN("140274577X");
        assertTrue(result);
    }

    @Test
    void checkValid13CharsISBNCode() {
        ISBNValidator validator = new ISBNValidator();
        boolean result = validator.validateISBN("9781402745775");
        assertTrue(result);
    }

}
