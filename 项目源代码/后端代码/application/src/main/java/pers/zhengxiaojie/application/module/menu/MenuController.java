package pers.zhengxiaojie.application.module.menu;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pers.zhengxiaojie.application.module.employee.Employee;
import pers.zhengxiaojie.application.module.employee.EmployeeService;
import pers.zhengxiaojie.application.module.result.Result;
import pers.zhengxiaojie.application.module.role.Role;
import pers.zhengxiaojie.application.module.role.RoleService;
import pers.zhengxiaojie.application.module.role_menu.RoleMenu;
import pers.zhengxiaojie.application.module.role_menu.RoleMenuService;
import pers.zhengxiaojie.application.utils.ResultUtil;
import pers.zhengxiaojie.application.utils.TokenUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 *
 */
@RestController
@RequestMapping(value = "/menu")
public class MenuController {
  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private MenuService menuService;

  @Autowired
  private RoleService roleService;

  @Autowired
  private RoleMenuService roleMenuService;

  @Value("${spring.security.token.signature}")
  private String signature;

  @PostMapping
  @PreAuthorize("hasAuthority('MANAGEMENT:DEPARTMENT:POST_SINGLE')")
  public Result postMenu(@RequestBody Menu menu) {

    System.out.println(menu.getLevel());
    if (menu.getLevel()== 1) {
      menu.setComponent(null);
      menu.setParentId(0);
    }

    if (menu.getLevel() == 2) {

    }
    menuService.save(menu); /*保存菜单*/

    QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
    queryWrapper.ne("id", 1);
    queryWrapper.ne("id", 2);
    List<Role> list = roleService.list(queryWrapper);

    list.stream().forEach(item -> {
      roleMenuService.save(new RoleMenu(item.getId(),menu.getId(), 0));
    });

    return ResultUtil.success(null, "添加菜单信息成功");
  }

  @DeleteMapping(value = "/{id}")
  public Result deleteMenuSingle(@PathVariable(value = "id") Integer id) {
    menuService.removeById(id);

    return ResultUtil.success(null, "删除菜单信息成功");
  }

  @DeleteMapping
  @Transactional
  public Result deleteMenuMultiple(@RequestBody List<Integer> ids) {
    menuService.removeBatchByIds(ids);

    return ResultUtil.success(null, "删除菜单信息成功");
  }

  @GetMapping(value = "/{id}")
  public Result getDepartmentMultiple(@PathVariable(value = "id") Integer id) {
    Menu menu = menuService.getById(id);
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("menu", menu);

    return ResultUtil.success(data, "获取菜单信息成功");
  }

//  获取所有菜单信息
  @GetMapping
  public Result getMenu() {
    List<Menu> menu = menuService.list();
    List<MenuDTO> level1Menu = menu.stream()
      .filter(item -> item.getLevel().equals(1))
      .sorted(Comparator.comparing(Menu::getOrderNum))
      .map(item -> new MenuDTO(
        item.getId(),
        item.getTitle(),
        item.getPath(),
        item.getComponent(),
        item.getParentId(),
        item.getOrderNum(),
        item.getLevel(),
        item.getTag(),
        item.getStatus(),
        null
      ))
      .collect(Collectors.toList());

    //    整理一级菜单与二级菜单
    for (MenuDTO currentLevel1Menu : level1Menu) {
      List<MenuDTO> level2Menu = menu.stream()
        .filter(item -> item.getParentId().equals(currentLevel1Menu.getId()))
        .sorted(Comparator.comparing(Menu::getOrderNum))
        .map(item -> new MenuDTO(
          item.getId(),
          item.getTitle(),
          item.getPath(),
          item.getComponent(),
          item.getParentId(),
          item.getOrderNum(),
          item.getLevel(),
          item.getTag(),
          item.getStatus(),
          null
        ))
        .collect(Collectors.toList());

//      整理二级菜单与三级菜单
      for (MenuDTO currentLevel2Menu : level2Menu) {
        List<MenuDTO> level3Menu = menu.stream()
          .filter(item -> item.getParentId().equals(currentLevel2Menu.getId()))
          .sorted(Comparator.comparing(Menu::getOrderNum))
          .map(item -> new MenuDTO(
            item.getId(),
            item.getTitle(),
            item.getPath(),
            item.getComponent(),
            item.getParentId(),
            item.getOrderNum(),
            item.getLevel(),
            item.getTag(),
            item.getStatus(),
            null
          ))
          .collect(Collectors.toList());

        currentLevel2Menu.setChildren(level3Menu);
      }
      currentLevel1Menu.setChildren(level2Menu);
    }

    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("menu", level1Menu);

    return ResultUtil.success(data, "获取菜单成功");
  }

  //根据Token获取相对应的菜单信息，如一级菜单，二级菜单，
  @GetMapping(value = "/token")
  public Result getMenu(@RequestHeader(value = "token") String token) throws UnsupportedEncodingException, JsonProcessingException {
    ConcurrentHashMap<String, Object> payloadData = TokenUtil.getTokenPayloadData(token, signature);
    String username = (String) payloadData.get("username");
    Employee employee = employeeService.getEmployeeByUsername(username);
    Integer roleId = employee.getRoleId();
    List<Menu> menu = menuService.getMenu(roleId);
    List<String> permissionList = new ArrayList<>();

    for (Menu menu1 : menu) {
      permissionList.add(new String(menu1.getTag()));
    }
    System.out.println(permissionList);
    List<MenuDTO> level1Menu = menu.stream()
      .filter(item -> item.getLevel().equals(1))
      .sorted(Comparator.comparing(Menu::getOrderNum))
      .map(item -> new MenuDTO(
        item.getId(),
        item.getTitle(),
        item.getPath(),
        item.getComponent(),
        item.getParentId(),
        item.getOrderNum(),
        item.getLevel(),
        item.getTag(),
        item.getStatus(),
        null
      ))
      .collect(Collectors.toList());

    //    整理一级菜单与二级菜单
    for (MenuDTO currentLevel1Menu : level1Menu) {
      List<MenuDTO> level2Menu = menu.stream()
        .filter(item -> item.getParentId().equals(currentLevel1Menu.getId()))
        .sorted(Comparator.comparing(Menu::getOrderNum))
        .map(item -> new MenuDTO(
          item.getId(),
          item.getTitle(),
          item.getPath(),
          item.getComponent(),
          item.getParentId(),
          item.getOrderNum(),
          item.getLevel(),
          item.getTag(),
          item.getStatus(),
          null
        ))
        .collect(Collectors.toList());

//      整理二级菜单与三级菜单
      for (MenuDTO currentLevel2Menu : level2Menu) {
        List<MenuDTO> level3Menu = menu.stream()
          .filter(item -> item.getParentId().equals(currentLevel2Menu.getId()))
          .sorted(Comparator.comparing(Menu::getOrderNum))
          .map(item -> new MenuDTO(
            item.getId(),
            item.getTitle(),
            item.getPath(),
            item.getComponent(),
            item.getParentId(),
            item.getOrderNum(),
            item.getLevel(),
            item.getTag(),
            item.getStatus(),
            null
          ))
          .collect(Collectors.toList());

        currentLevel2Menu.setChildren(level3Menu);
      }
      currentLevel1Menu.setChildren(level2Menu);
    }

    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("menu", level1Menu);
    data.put("permission", permissionList);

    return ResultUtil.success(data, "获取菜单成功");
  }

  @PutMapping
  public Result putDepartment(@RequestBody Menu menu) {
    menuService.updateById(menu);

    return ResultUtil.success(null, "修改菜单信息成功");
  }

  @GetMapping(value = "/test/{roleId}")
  public Result getMenuByRoleId(@PathVariable(value = "roleId") Integer roleId) {
    List<Menu> menu = menuService.getMenu(roleId);
    List<String> permissionList = new ArrayList<>();

    for (Menu menu1 : menu) {
      permissionList.add(new String(menu1.getTag()));
    }
    System.out.println(permissionList);
    List<MenuDTO> level1Menu = menu.stream()
      .filter(item -> item.getLevel().equals(1))
      .sorted(Comparator.comparing(Menu::getOrderNum))
      .map(item -> new MenuDTO(
        item.getId(),
        item.getTitle(),
        item.getPath(),
        item.getComponent(),
        item.getParentId(),
        item.getOrderNum(),
        item.getLevel(),
        item.getTag(),
        item.getStatus(),
        null
      ))
      .collect(Collectors.toList());

    //    整理一级菜单与二级菜单
    for (MenuDTO currentLevel1Menu : level1Menu) {
      List<MenuDTO> level2Menu = menu.stream()
        .filter(item -> item.getParentId().equals(currentLevel1Menu.getId()))
        .sorted(Comparator.comparing(Menu::getOrderNum))
        .map(item -> new MenuDTO(
          item.getId(),
          item.getTitle(),
          item.getPath(),
          item.getComponent(),
          item.getParentId(),
          item.getOrderNum(),
          item.getLevel(),
          item.getTag(),
          item.getStatus(),
          null
        ))
        .collect(Collectors.toList());

//      整理二级菜单与三级菜单
      for (MenuDTO currentLevel2Menu : level2Menu) {
        List<MenuDTO> level3Menu = menu.stream()
          .filter(item -> item.getParentId().equals(currentLevel2Menu.getId()))
          .sorted(Comparator.comparing(Menu::getOrderNum))
          .map(item -> new MenuDTO(
            item.getId(),
            item.getTitle(),
            item.getPath(),
            item.getComponent(),
            item.getParentId(),
            item.getOrderNum(),
            item.getLevel(),
            item.getTag(),
            item.getStatus(),
            null
          ))
          .collect(Collectors.toList());

        currentLevel2Menu.setChildren(level3Menu);
      }
      currentLevel1Menu.setChildren(level2Menu);
    }

    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("menu", level1Menu);
    data.put("permission", permissionList);

    return ResultUtil.success(data, "获取菜单成功");
  }

  /**
   * 获取二级组件
   * @return
   * Result 响应结果
   */
  @GetMapping(value = "/level2")
  public Result getComponents() {
    ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();

    QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();

    queryWrapper.eq("level", "2");
    queryWrapper.between("id", 1, 39);

    List<Menu> list = menuService.list(queryWrapper);

    concurrentHashMap.put("menu", list);
    return ResultUtil.success(concurrentHashMap, "获取组件成功");
  }

  @GetMapping(value = "/level12")
  public Result getComponentsD() {
    ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();

    QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();

    queryWrapper.eq("level", "1");

    List<Menu> list = menuService.list(queryWrapper);

    concurrentHashMap.put("menu", list);
    return ResultUtil.success(concurrentHashMap, "获取菜单成功成功");
  }
}