package pers.zhengxiaojie.application.config.spring_security.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class EmployeeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  public EmployeeAuthenticationFilter() {
    super(new AntPathRequestMatcher("/employee/login", "POST"));
  }

  // 自定义UsernamePasswordAuthenticationFilter，实现JSON方式登录
  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
    if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE) ) {
      ObjectMapper mapper = new ObjectMapper();
      InputStream is = request.getInputStream();
      Map<String, String> authenticationBean = mapper.readValue(is, Map.class);
      EmployeeAuthenticationToken employeeAuthenticationToken = new EmployeeAuthenticationToken(authenticationBean.get("username"), authenticationBean.get("password"));
      return this.getAuthenticationManager().authenticate(employeeAuthenticationToken);
    } else {
      throw new AuthenticationServiceException("当前认证服务只支持POST方式请求以及JSON格式数据");
    }
  }
}
