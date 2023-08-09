package br.com.compasso.posthistoryapi.exceptions;

public class PostNotFoundException extends RuntimeException {
  public PostNotFoundException(String message) {
    super(message);
  }
}
