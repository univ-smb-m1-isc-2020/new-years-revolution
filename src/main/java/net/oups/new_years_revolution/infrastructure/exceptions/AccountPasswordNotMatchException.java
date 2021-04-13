package net.oups.new_years_revolution.infrastructure.exceptions;

public class AccountPasswordNotMatchException extends Exception {
    public AccountPasswordNotMatchException(String message) {
        super(message);
    }
}
