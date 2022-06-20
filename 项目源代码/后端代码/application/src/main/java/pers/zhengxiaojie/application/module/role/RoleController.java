package pers.zhengxiaojie.application.module.role;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pers.zhengxiaojie.application.module.employee.Employee;
import pers.zhengxiaojie.application.module.employee.EmployeeService;
import pers.zhengxiaojie.application.module.menu.Menu;
import pers.zhengxiaojie.application.module.menu.MenuService;
import pers.zhengxiaojie.application.module.result.Result;
import pers.zhengxiaojie.application.module.role_menu.RoleMenu;
import pers.zhengxiaojie.application.module.role_menu.RoleMenuService;
import pers.zhengxiaojie.application.utils.ResultUtil;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 角色类
 */
@RestController
@RequestMapping(value = "role")
public class RoleController {
  @Autowired
  private RoleService roleService;

  @Autowired
  private MenuService menuService;

  @Autowired
  private RoleMenuService roleMenuService;

  @Autowired
  private EmployeeService employeeService;

  /**
   * 添加角色
   * @param
   * role 角色
   */
  @PostMapping
  @Transactional
  @PreAuthorize("hasAuthority('MANAGEMENT:ROLE:POST_SINGLE')")
  public Result postRole(@RequestBody Role role) {
    roleService.save(role);
    List<Menu> menuList = menuService.list();

    for (Menu menu : menuList) {
      roleMenuService.save(new RoleMenu(role.getId(), menu.getId(), 1));
    }

    return ResultUtil.success(null, "添加角色信息成功");
  }

  @DeleteMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('MANAGEMENT:ROLE:DELETE_SINGLE')")
  public Result deleteRoleSingle(@PathVariable(value = "id") Integer id) {
    roleService.removeById(id);

    UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
    updateWrapper.eq("role_id", id);
    updateWrapper.set("role_id", 1);

    employeeService.update(updateWrapper);


    return ResultUtil.success(null, "删除角色信息成功");
  }

  @DeleteMapping
  @PreAuthorize("hasAuthority('MANAGEMENT:ROLE:DELETE_MULTIPLE')")
  @Transactional
  public Result deleteRoleMultiple(@RequestBody List<Integer> ids) {
    roleService.removeBatchByIds(ids);

    UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
    updateWrapper.eq("role_id", ids);
    updateWrapper.set("role_id", 1);

    employeeService.update(updateWrapper);

    return ResultUtil.success(null, "删除角色信息成功");
  }

  @GetMapping
  public Result getRoleMultiple() {
    List<Role> role = roleService.list();
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("role", role);

    return ResultUtil.success(data, "获取角色信息成功");
  }

  @GetMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('MANAGEMENT:ROLE:GET_MULTIPLE')")
  public Result getRoleMultiple(@PathVariable(value = "id") Integer id) {
    Role role = roleService.getById(id);
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("role", role);

    return ResultUtil.success(data, "获取角色信息成功");
  }

  @GetMapping(value = "/{current}/{size}")
  @PreAuthorize("hasAuthority('MANAGEMENT:ROLE:GET_MULTIPLE')")
  public Result getRoleMultiple(@PathVariable(value = "current") Integer current, @PathVariable(value = "size") Integer size) {
    IPage<Role> role = roleService.page(new Page<>(current, size));
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("rolePage", role);

    return ResultUtil.success(data, "获取角色信息成功");
  }

  @GetMapping(value = "/{current}/{size}/{name}")
  @PreAuthorize("hasAuthority('MANAGEMENT:ROLE:GET_MULTIPLE')")
  public Result getRoleMultiple(@PathVariable(value = "current") Integer current, @PathVariable(value = "size") Integer size,  @PathVariable(value = "name") String name) {
    QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();

    roleQueryWrapper.like("name", '%' + name + '%');
    IPage<Role> role = roleService.page(new Page<>(current, size), roleQueryWrapper);
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("rolePage", role);

    return ResultUtil.success(data, "获取角色信息成功");
  }

  @PutMapping
  @PreAuthorize("hasAuthority('MANAGEMENT:ROLE:UPDATE_SINGLE')")
  public Result putRole(@RequestBody Role role) {
    roleService.updateById(role);

    return ResultUtil.success(null, "修改角色信息成功");
  }
}
