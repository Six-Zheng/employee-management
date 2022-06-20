package pers.zhengxiaojie.application.module.role_menu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pers.zhengxiaojie.application.module.result.Result;
import pers.zhengxiaojie.application.utils.ResultUtil;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(value = "/role_menu")
public class RoleMenuController {
  @Autowired
  private RoleMenuService roleMenuService;

  @PostMapping
  public Result roleMenuRoleMenu(@RequestBody RoleMenu roleMenu) {
    roleMenu.setStatus(1);
    roleMenuService.save(roleMenu);

    return ResultUtil.success(null, "添加角色菜单信息成功");
  }

  @DeleteMapping(value = "/{id}")
  public Result deleteRoleMenuSingle(@PathVariable(value = "id") Integer id) {
    roleMenuService.removeById(id);

    return ResultUtil.success(null, "删除角色菜单信息成功");
  }

  @DeleteMapping
  @Transactional
  public Result deleteRoleMenuMultiple(@RequestBody List<Integer> ids) {
    roleMenuService.removeBatchByIds(ids);

    return ResultUtil.success(null, "删除角色菜单信息成功");
  }

  @GetMapping(value = "/{id}")
  public Result getRoleMenuMultiple(@PathVariable(value = "id") Integer id) {
    RoleMenu roleMenu = roleMenuService.getById(id);
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("roleMenu", roleMenu);

    return ResultUtil.success(data, "获取角色菜单信息成功");
  }

  //通过roleId获取其已经获取的权限
  @GetMapping(value = "/role_id/{role_id}")
  public Result getRoleMenuMultipleByRoleId(@PathVariable(value = "role_id") Integer roleId) {
    QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();

    queryWrapper.eq("role_id", roleId);
    queryWrapper.eq("status", 0);

    List<RoleMenu> list = roleMenuService.list(queryWrapper);
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("roleMenu", list);

    return ResultUtil.success(data, "获取角色菜单信息成功");
  }

  @GetMapping(value = "/{current}/{size}")
  public Result getRoleMenuMultiple(@PathVariable(value = "current") Integer current, @PathVariable(value = "size") Integer size) {
    IPage<RoleMenu> roleMenu = roleMenuService.page(new Page<>(current, size));
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("roleMenuPage", roleMenu);

    return ResultUtil.success(data, "获取角色菜单信息成功");
  }

  @GetMapping(value = "/{current}/{size}/{name}")
  public Result getRoleMenuMultiple(@PathVariable(value = "current") Integer current, @PathVariable(value = "size") Integer size,  @PathVariable(value = "name") String name) {
    QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();

    roleMenuQueryWrapper.like("name", '%' + name + '%');
    IPage<RoleMenu> roleMenu = roleMenuService.page(new Page<>(current, size), roleMenuQueryWrapper);
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("roleMenuPage", roleMenu);

    return ResultUtil.success(data, "获取角色菜单信息成功");
  }

  //通过roleId修改其已经获取的权限,ids是已分配的权限
  @PutMapping(value = "/role_id/{role_id}")
  public Result putRoleMenu(@PathVariable(value = "role_id")Integer roleId,  @RequestBody List<Integer> ids) {
    List<RoleMenu> list = roleMenuService.selectMenu(roleId);

    list.forEach(item -> {
      if (ids.contains(item.getMenuId()))
        item.setStatus(0);
      else
        item.setStatus(1);
    });
    roleMenuService.updateBatchById(list);



    return ResultUtil.success(null, "修改角色菜单信息成功");
  }
}
