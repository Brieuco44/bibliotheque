package fr.tdd.exception;

public class QuotaLivreDepasseException extends RuntimeException{
    public QuotaLivreDepasseException(String message) {
        super(message);
    }
}


