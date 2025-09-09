package io.github.PercivalGebashe.authentication_authorization.exception;

public class AccountNotVerifiedException extends RuntimeException {
    public AccountNotVerifiedException() {
        super("Account is not verified");
    }

    public AccountNotVerifiedException(String message) {
        super(message);
    }
}