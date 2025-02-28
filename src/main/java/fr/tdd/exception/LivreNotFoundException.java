package fr.tdd.exception;

import jakarta.persistence.EntityNotFoundException;

public class LivreNotFoundException extends EntityNotFoundException{
        public LivreNotFoundException(String message) {
            super(message);
        }
}
