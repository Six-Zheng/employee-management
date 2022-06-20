package pers.zhengxiaojie.application.module.menu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import pers.zhengxiaojie.application.module.role.RoleMapper;
import pers.zhengxiaojie.application.module.role_menu.RoleMenu;
import pers.zhengxiaojie.application.module.role_menu.RoleMenuMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
  @Autowired
  private MenuMapper menuMapper;

  @Autowired
  private RoleMapper roleMapper;

  @Autowired
  private RoleMenuMapper roleMenuMapper;

  @Override
  public List<SimpleGrantedAuthority> getGrantedAuthorityByRoleId(Integer roleId) {
    QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();

    roleMenuQueryWrapper.lambda().eq(RoleMenu::getRoleId, roleId).eq(RoleMenu::getIsDeleted, 0);

    List<Integer> menuIds = roleMenuMapper.selectList(roleMenuQueryWrapper)
      .stream()
      .map(RoleMenu::getMenuId)
      .collect(Collectors.toList());

    List<String> tags = menuMapper.selectBatchIds(menuIds)
      .stream()
      .map(Menu::getTag)
      .collect(Collectors.toList());

    tags.add(roleMapper.selectById(roleId).getTag());

    List<SimpleGrantedAuthority> simpleGrantedAuthority = tags.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

    return simpleGrantedAuthority;
  }

  @Override
  public List<Menu> getMenu(Integer roleId) {
    QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();

    roleMenuQueryWrapper.lambda().eq(RoleMenu::getRoleId, roleId).eq(RoleMenu::getIsDeleted, 0)
    .eq(RoleMenu::getStatus, 0);

    List<Integer> menuIds = roleMenuMapper.selectList(roleMenuQueryWrapper)
      .stream()
      .map(RoleMenu::getMenuId)
      .collect(Collectors.toList());

    List<Menu> menu = menuMapper.selectBatchIds(menuIds);

    return menu;
  }

  @Override
  public void test() {
    System.out.println(roleMenuMapper.selectList(null));
  }
}
