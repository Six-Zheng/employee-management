package pers.zhengxiaojie.application.exception;

import org.springframework.security.core.AuthenticationException;

public class VerificationCodeAuthenticationException extends AuthenticationException {
  public VerificationCodeAuthenticationException(String msg) {
    super(msg);
  }
}
