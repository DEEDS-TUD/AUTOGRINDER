package de.grinder.executor;

public class TargetCreationException extends Exception {
  private static final long serialVersionUID = 1L;
  
  public TargetCreationException() {
    super();
  }

  public TargetCreationException(String message) {
    super(message);
  }

  public TargetCreationException(String message, Throwable cause) {
    super(message, cause);
  }

  public TargetCreationException(Throwable cause) {
    super(cause);
  }

}
