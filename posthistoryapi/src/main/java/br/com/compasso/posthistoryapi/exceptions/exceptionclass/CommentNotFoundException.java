package br.com.compasso.posthistoryapi.exceptions.exceptionclass;
public class CommentNotFoundException extends RuntimeException {
  public CommentNotFoundException(String message) {
    super(message);
  }
}
