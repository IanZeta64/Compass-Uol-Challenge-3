package br.com.compasso.posthistoryapi.exceptions.exceptionclass;
public class HistoryNotFoundException extends RuntimeException {
  public HistoryNotFoundException(String message) {
    super(message);
  }
}
