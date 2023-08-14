package br.com.compasso.posthistoryapi.exceptions.exceptionclass;
public class DuplicatePostException extends RuntimeException {
  public DuplicatePostException(String message) {
    super(message);
  }
}
