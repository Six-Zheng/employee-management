package pers.zhengxiaojie.application.exception;

public class VerificationCodeTimeOutException extends RuntimeException {
  public VerificationCodeTimeOutException(String message) {
    super(message);
  }
}
