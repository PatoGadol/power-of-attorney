package nl.cinq.rabo.exceptions;

public class CustomException extends Exception{
    public CustomException(String errorMessage) {
        super(errorMessage);
    }
}
