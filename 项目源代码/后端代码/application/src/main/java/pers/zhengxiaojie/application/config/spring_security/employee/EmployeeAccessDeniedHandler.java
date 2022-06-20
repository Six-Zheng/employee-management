package pers.zhengxiaojie.application.config.spring_security.employee;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import pers.zhengxiaojie.application.module.result.Result;
import pers.zhengxiaojie.application.utils.ResultUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class EmployeeAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

    ServletOutputStream outputStream = response.getOutputStream();

    Result result = ResultUtil.failure(HttpStatus.HTTP_FORBIDDEN, null, "当前用户无权限访问");
    String resultJsonObject = JSON.toJSONString(result);

    outputStream.write(resultJsonObject.getBytes("UTF-8"));
    outputStream.flush();
    outputStream.close();
  }
}
