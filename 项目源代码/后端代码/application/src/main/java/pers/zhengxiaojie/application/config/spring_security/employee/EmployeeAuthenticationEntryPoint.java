package pers.zhengxiaojie.application.config.spring_security.employee;

import org.springframework.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import pers.zhengxiaojie.application.module.result.Result;
import pers.zhengxiaojie.application.utils.ResultUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class EmployeeAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    System.out.println(request.getRequestURI());
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    ServletOutputStream outputStream = response.getOutputStream();

    System.out.println(authException.getMessage());
    Result result = ResultUtil.failure(HttpStatus.UNAUTHORIZED.value(), null, authException.getMessage() + "请登录系统");
    String resultJsonObject = JSON.toJSONString(result);

    outputStream.write(resultJsonObject.getBytes("UTF-8"));
    outputStream.flush();
    outputStream.close();
  }
}
