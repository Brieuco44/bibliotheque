package fr.tdd.exception;

import jakarta.persistence.EntityNotFoundException;

public class LivreDejaExistantException extends EntityNotFoundException{
        public LivreDejaExistantException(String message) {
            super(message);
        }
}
