package br.com.compasso.posthistoryapi.exceptions.exceptionclass;
public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String message) {
        super((message));
    }
}
