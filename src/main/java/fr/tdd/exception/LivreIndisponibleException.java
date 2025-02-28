package fr.tdd.exception;

import jakarta.persistence.EntityNotFoundException;

public class LivreIndisponibleException extends EntityNotFoundException {
    public LivreIndisponibleException(String message) {
        super(message);
    }

}
