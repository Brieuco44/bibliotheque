package fr.tdd.exception;

import jakarta.persistence.EntityNotFoundException;

public class IsbnInvalideException extends EntityNotFoundException{
    public IsbnInvalideException(String message) {
        super(message);
    }
}
