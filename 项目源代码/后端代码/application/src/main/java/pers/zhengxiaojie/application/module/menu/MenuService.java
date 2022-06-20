package pers.zhengxiaojie.application.module.menu;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public interface MenuService extends IService<Menu>{

//  根据角色ID获取对应的权限信息
  List<SimpleGrantedAuthority> getGrantedAuthorityByRoleId(Integer roleId);

//  根据角色ID获取对应的菜单信息
  List<Menu> getMenu(Integer roleId);

  void test();
}
