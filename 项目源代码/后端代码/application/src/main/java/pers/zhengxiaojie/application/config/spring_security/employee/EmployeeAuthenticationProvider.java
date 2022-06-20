package pers.zhengxiaojie.application.config.spring_security.employee;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pers.zhengxiaojie.application.module.employee.Employee;
import pers.zhengxiaojie.application.module.employee.EmployeeService;
import pers.zhengxiaojie.application.module.menu.MenuService;
import pers.zhengxiaojie.application.utils.MD5Util;

import java.util.List;

//用户名于密码登录认证器
@Component
public class EmployeeAuthenticationProvider implements AuthenticationProvider {
  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private MenuService menuService;

  @Value("${spring.security.md5.interations}")
  private Integer interations;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = (String) authentication.getPrincipal();
    String password = (String) authentication.getCredentials();
    Employee employee = employeeService.getEmployeeByUsername(username);

    if (ObjectUtil.isEmpty(employee)) {
      throw new UsernameNotFoundException("用户不存在，请重新输入用户名！");
    } else {
      Boolean validationResult = MD5Util.verifyMD5IsValid(
        employee.getPassword(),
        password,
        employee.getSalt(),
        interations
      );
      if (validationResult) {
        Integer roleId = employee.getRoleId();
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = menuService.getGrantedAuthorityByRoleId(1);

        return (Authentication) new EmployeeAuthenticationToken(employee.getUsername(), employee.getPassword(), simpleGrantedAuthorities);
      } else {
        throw new BadCredentialsException("用户名或密码输入有误，请重新输入！");
      }
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return true;
//    return EmployeeAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
