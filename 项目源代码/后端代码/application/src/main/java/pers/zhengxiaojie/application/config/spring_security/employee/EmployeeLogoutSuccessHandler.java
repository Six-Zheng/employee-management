package pers.zhengxiaojie.application.config.spring_security.employee;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import pers.zhengxiaojie.application.module.result.Result;
import pers.zhengxiaojie.application.utils.ResultUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EmployeeLogoutSuccessHandler implements LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    if (authentication != null) {
      new SecurityContextLogoutHandler().logout(request, response, authentication);
    }

    ServletOutputStream outputStream = response.getOutputStream();
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    response.setContentType("application/json;charset=UTF-8");

    Result result = ResultUtil.success(null, "用户退出成功");
    String resultJsonObject = JSON.toJSONString(result);

    response.setHeader("token", "");
    outputStream.write(resultJsonObject.getBytes("UTF-8"));
    outputStream.flush();
    outputStream.close();
  }
}
