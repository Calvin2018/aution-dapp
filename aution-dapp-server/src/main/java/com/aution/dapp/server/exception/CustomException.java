package com.aution.dapp.server.exception;

public class CustomException extends Exception {
  private Integer code;

  /**
   * Constructor with error message.
   *
   * @param message the error message.
   */
  public CustomException(String message) {
    super(message);
  }

  /**
   * Constructor with underline exception.
   *
   * @param e the underline exception
   */
  public CustomException(Exception e) {
    super(e.getMessage(),e);
    this.code = 500;
  }

  /**
   * Constructor with error message and root cause.
   *
   * @param message the error message
   * @param cause the root cause
   */
  public CustomException(String message, Throwable cause) {
    super(message, cause);
    this.code = 500;
  }

  /**
   * Constructor with error code and message.
   *
   * @param code the error code
   * @param message the error message
   */
  public CustomException(int code, String message) {
    super(message);
    this.code = code;
  }

  /**
   * Constructor with error code, message and cause.
   *
   * @param code
   * @param message
   * @param cause
   */
  public CustomException(int code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  /**
   * Get the error code.
   *
   * @return the error code.
   */
  public Integer getCode() {
    return code;
  }
}
