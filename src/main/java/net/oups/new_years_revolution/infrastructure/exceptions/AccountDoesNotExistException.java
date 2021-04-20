package net.oups.new_years_revolution.infrastructure.exceptions;

public class AccountDoesNotExistException extends Exception {
    public AccountDoesNotExistException(String message) {
        super(message);
    }
}
