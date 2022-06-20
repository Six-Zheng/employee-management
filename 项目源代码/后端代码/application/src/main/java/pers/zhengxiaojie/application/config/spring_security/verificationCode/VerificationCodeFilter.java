package pers.zhengxiaojie.application.config.spring_security.verificationCode;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;
import pers.zhengxiaojie.application.exception.VerificationCodeAuthenticationException;
import pers.zhengxiaojie.application.extension.CustomizeHttpServletRequestWrapper;
import pers.zhengxiaojie.application.utils.RedisUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class VerificationCodeFilter extends OncePerRequestFilter {
  @Autowired
  private RedisUtil redisUtil;

  @Autowired
  private AuthenticationEntryPoint authenticationEntryPoint;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    if (request.getRequestURI().equals("/employee/avatar")) {
      filterChain.doFilter(request, response);
      return;
    }

    if ("/employee/login".equals(request.getRequestURI())) {
      request.getContentType();
      if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE) || request.getMethod().equals("POST")) {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = request.getInputStream();
        Map<String, String> authenticationBean = mapper.readValue(is, Map.class);
        String verificationCodeKey = request.getHeader("verification-code-key");
        String verificationCode = (String) redisUtil.getValue("login-" + verificationCodeKey);

        if (StrUtil.isBlankOrUndefined(verificationCode)) {
          this.authenticationEntryPoint.commence(request, response, new VerificationCodeAuthenticationException("验证码获取已" +
            "失效，请重新登录"));
        }
        if (verificationCode.equals(authenticationBean.get("verificationCode"))) {
          redisUtil.deleteKey("login-" + verificationCodeKey);
          doFilter(request, response, filterChain);
        } else {
          redisUtil.deleteKey("login-" + verificationCodeKey);
          this.authenticationEntryPoint.commence(request, response, new VerificationCodeAuthenticationException("验证码输入有误！请重新输入"));
        }
      } else {
        this.authenticationEntryPoint.commence(request, response, new AuthenticationServiceException("当前认证服务只支持POST方式请求以及JSON格式数据"));
      }
    } else {
      doFilter(new CustomizeHttpServletRequestWrapper(request), response, filterChain);
      return;
    }
  }
}
