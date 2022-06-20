package pers.zhengxiaojie.application.config.spring_security.employee;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pers.zhengxiaojie.application.module.result.Result;
import pers.zhengxiaojie.application.utils.RedisUtil;
import pers.zhengxiaojie.application.utils.ResultUtil;
import pers.zhengxiaojie.application.utils.TokenUtil;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class EmployeeLoginSuccessHandler implements AuthenticationSuccessHandler {
  @Value("${spring.security.token.signature}")
  private String signature;

  @Value("${spring.security.token.expiration-time}")
  private Integer Expiretime;

  @Autowired
  private RedisUtil redisUtil;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    response.setContentType("application/json;charset=UTF-8");

    ServletOutputStream outputStream = response.getOutputStream();
    String username = (String) authentication.getPrincipal();

//    登录成功后,为请求头添加参数token
    ConcurrentHashMap<String, Object> payload = new ConcurrentHashMap();
    Calendar calendar = Calendar.getInstance();

    calendar.add(Calendar.MINUTE, Expiretime);
    payload.put("username", username);

    String token = TokenUtil.getToken(calendar, payload, signature);

    response.setHeader("token", token);

//    登录成功后,缓存用户权限信息到Redis
    String tag = "";

    for (GrantedAuthority authority : authentication.getAuthorities()) {
      tag = tag + authority.getAuthority() + ",";
    }
    tag = tag.substring(0, tag.length() - 1);
    redisUtil.setKeyAndValue("GrantedAuthorities-" + username, tag, TimeUnit.MINUTES, 30);

    Result result = ResultUtil.success(null, "登录成功");
    String resultToJsonObject = JSON.toJSONString(result);

    outputStream.write(resultToJsonObject.getBytes("UTF-8"));
    outputStream.flush();
    outputStream.close();
  }
}
