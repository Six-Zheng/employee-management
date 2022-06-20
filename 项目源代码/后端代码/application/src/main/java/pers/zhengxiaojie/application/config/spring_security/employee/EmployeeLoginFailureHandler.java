package pers.zhengxiaojie.application.config.spring_security.employee;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import pers.zhengxiaojie.application.module.result.Result;
import pers.zhengxiaojie.application.utils.ResultUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class EmployeeLoginFailureHandler implements AuthenticationFailureHandler {
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    ServletOutputStream outputStream = response.getOutputStream();

    Result result = ResultUtil.failure(HttpStatus.UNAUTHORIZED.value(), null, exception.getMessage());
    String resultToJsonObject = JSON.toJSONString(result);
    outputStream.write(resultToJsonObject.getBytes("UTF-8"));
    outputStream.flush();
    outputStream.close();
  }
}