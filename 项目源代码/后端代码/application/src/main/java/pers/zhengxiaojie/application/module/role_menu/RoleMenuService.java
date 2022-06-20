package pers.zhengxiaojie.application.module.role_menu;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleMenuService extends IService<RoleMenu> {
  void recoveryById(Integer id);
  List<RoleMenu> selectMenu(Integer roleId);;
}
