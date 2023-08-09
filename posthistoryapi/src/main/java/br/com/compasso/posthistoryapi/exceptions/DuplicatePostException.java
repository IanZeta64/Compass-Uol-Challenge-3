package br.com.compasso.posthistoryapi.exceptions;

public class DuplicatePostException extends RuntimeException {
  public DuplicatePostException(String message) {
    super(message);
  }
}
