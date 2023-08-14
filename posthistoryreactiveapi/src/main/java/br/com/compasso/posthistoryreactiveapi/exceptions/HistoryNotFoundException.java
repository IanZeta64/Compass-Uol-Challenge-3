package br.com.compasso.posthistoryreactiveapi.exceptions;

public class HistoryNotFoundException extends RuntimeException {
  public HistoryNotFoundException(String message) {
    super(message);
  }
}
