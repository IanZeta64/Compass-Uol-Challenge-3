package br.com.compasso.userauthenticationapi.exceptions;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String s) {
        super((s));
    }
}
