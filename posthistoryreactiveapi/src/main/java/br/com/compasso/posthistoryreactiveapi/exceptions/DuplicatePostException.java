package br.com.compasso.posthistoryreactiveapi.exceptions;

public class DuplicatePostException extends RuntimeException {
  public DuplicatePostException(String message) {
    super(message);
  }
}
