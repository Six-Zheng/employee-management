package pers.zhengxiaojie.application.exception;

public class NoEmailException extends RuntimeException {
  public NoEmailException(String message) {
    super(message);
  }
}
