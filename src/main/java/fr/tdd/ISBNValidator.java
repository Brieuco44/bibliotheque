package fr.tdd;

import fr.tdd.exception.IsbnInvalideException;

public class ISBNValidator {

    private static final int LONG_ISBN_LENGTH = 13;
    private static final int SHORT_ISBN_LENGTH = 10;
    private static final int LONG_ISBN_DIVIDER = 10;
    private static final int SHORT_ISBN_DIVIDER = 11;

    public boolean validateISBN(String isbn) {
        if (isbn.length() == LONG_ISBN_LENGTH) {
            return validateLongISBN(isbn);
        }
        else if (isbn.length() == SHORT_ISBN_LENGTH) {
            return validateShortISBN(isbn);
        }

        throw new IsbnInvalideException("Un ISBN doit etre de 10 ou 13 chiffres.");
    }

    private boolean validateLongISBN(String isbn) {
        int total = 0;

        for (int i = 0; i < LONG_ISBN_LENGTH; i++) {
            if (i % 2 == 0) {
                total += Character.getNumericValue(isbn.charAt(i));
            } else {
                total += Character.getNumericValue(isbn.charAt(i)) * 3;
            }
        }

        return (total % LONG_ISBN_DIVIDER == 0);
    }

    private boolean validateShortISBN(String isbn) {
        int total = 0;

        for (int i = 0; i < SHORT_ISBN_LENGTH; i++) {
            if (!Character.isDigit(isbn.charAt(i))) {
                if (i == 9 && isbn.charAt(i) == 'X') {
                    total += 10;
                    break;
                } else {
                    throw new IsbnInvalideException("Un ISBN ne doit contenir seulement des chiffres.");
                }
            }

            total += Character.getNumericValue(isbn.charAt(i)) * (10 - i);
        }

        return (total % SHORT_ISBN_DIVIDER == 0);
    }

}
