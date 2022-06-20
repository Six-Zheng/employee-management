package pers.zhengxiaojie.application.config.spring_security.token;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;
import pers.zhengxiaojie.application.config.spring_security.employee.EmployeeAuthenticationToken;
import pers.zhengxiaojie.application.exception.TokenExpiredException;
import pers.zhengxiaojie.application.extension.CustomizeHttpServletRequestWrapper;
import pers.zhengxiaojie.application.utils.RedisUtil;
import pers.zhengxiaojie.application.utils.TokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TokenFilter extends OncePerRequestFilter {
  @Autowired
  private RedisUtil redisUtil;

  @Value("${spring.security.token.signature}")
  private String signature;

  @Autowired
  private AuthenticationEntryPoint authenticationEntryPoint;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    if (request.getRequestURI().equals("/employee/avatar")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = request.getHeader("token");
    if ("/employee/logout".equals(request.getRequestURI())) {
      filterChain.doFilter(new CustomizeHttpServletRequestWrapper(request), response);
      return; // 防止代码块继续往下执行
    }

//    判断请求是否存在Token,若不存在Token,则放行,让其他过滤器进行认证处理
    if (StrUtil.isBlankOrUndefined(token)) {
      filterChain.doFilter(new CustomizeHttpServletRequestWrapper(request), response);

      return; // 此处防止代码块继续往下执行
    }
    ConcurrentHashMap<String, Object> payload = null;
    try {
      payload = TokenUtil.getTokenPayloadData(token, signature);
    }catch (Exception e) {
      this.authenticationEntryPoint.commence(request, response, new TokenExpiredException("用户令牌已失效" +
        "失效，请重新登录"));
      throw e;
    }
    String username = (String) payload.get("username");

    // 从Redis中获取用户相关的权限信息
    String tag = (String) redisUtil.getValue("GrantedAuthorities-" + username);
    List<String> tags = Arrays.asList(tag.split(","));
    List<SimpleGrantedAuthority> simpleGrantedAuthorities = tags.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

    EmployeeAuthenticationToken employeeAuthenticationToken = new EmployeeAuthenticationToken(username, null, simpleGrantedAuthorities);
    SecurityContextHolder.getContext().setAuthentication(employeeAuthenticationToken);
    filterChain.doFilter(new CustomizeHttpServletRequestWrapper(request), response);
  }
}
