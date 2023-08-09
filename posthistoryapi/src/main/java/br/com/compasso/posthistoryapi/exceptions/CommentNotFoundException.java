package br.com.compasso.posthistoryapi.exceptions;

public class CommentNotFoundException extends RuntimeException {
  public CommentNotFoundException(String message) {
    super(message);
  }
}
