package pers.zhengxiaojie.application.module.role_menu;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
  @Autowired
  private RoleMenuMapper roleMenuMapper;

  @Override
  public void recoveryById(Integer id) {
    roleMenuMapper.recoveryById(id);
  }

  public List<RoleMenu> selectMenu(Integer roleId) {
    return roleMenuMapper.selectMenu(roleId);
  }
}
