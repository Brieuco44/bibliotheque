package fr.tdd.exception;

public class ReservationImpossibleException extends RuntimeException{
    public ReservationImpossibleException(String message) {
        super(message);
    }

}
