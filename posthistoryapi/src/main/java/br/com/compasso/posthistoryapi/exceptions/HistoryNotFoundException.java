package br.com.compasso.posthistoryapi.exceptions;

public class HistoryNotFoundException extends RuntimeException {
  public HistoryNotFoundException(String message) {
    super(message);
  }
}
