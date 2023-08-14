package br.com.compasso.posthistoryapi.exceptions.exceptionclass;
public class PostNotFoundException extends RuntimeException {
  public PostNotFoundException(String message) {
    super(message);
  }
}
