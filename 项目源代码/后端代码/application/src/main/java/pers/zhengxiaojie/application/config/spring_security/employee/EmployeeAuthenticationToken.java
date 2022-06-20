package pers.zhengxiaojie.application.config.spring_security.employee;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class EmployeeAuthenticationToken extends UsernamePasswordAuthenticationToken {
  private String username;

  private String password;

  private Collection<? extends GrantedAuthority> authorities;

  public EmployeeAuthenticationToken(String username, String password) {
    super(username, password);
  }

  public EmployeeAuthenticationToken(String username, String password, Collection<? extends GrantedAuthority> authorities) {
    super(username, password, authorities);
  }
}
