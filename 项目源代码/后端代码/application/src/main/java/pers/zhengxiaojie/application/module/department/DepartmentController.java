package pers.zhengxiaojie.application.module.department;

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
import pers.zhengxiaojie.application.module.result.Result;
import pers.zhengxiaojie.application.utils.ResultUtil;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(value = "/department")
public class DepartmentController {
  @Autowired
  private DepartmentService departmentService;

  @Autowired
  private EmployeeService employeeService;

  @PostMapping
  @PreAuthorize("hasAuthority('MANAGEMENT:DEPARTMENT:POST_SINGLE')")
  public Result postDepartment(@RequestBody Department department) {
    departmentService.save(department);

    return ResultUtil.success(null, "添加部门信息成功");
  }

  @DeleteMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('MANAGEMENT:DEPARTMENT:DELETE_SINGLE')")
  public Result deleteDepartmentSingle(@PathVariable(value = "id") Integer id) {
    departmentService.removeById(id);

    UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
    updateWrapper.eq("department_id", id);
    updateWrapper.set("department_id", 1);

    employeeService.update(updateWrapper);

    return ResultUtil.success(null, "删除部门信息成功");
  }

  @DeleteMapping
  @PreAuthorize("hasAuthority('MANAGEMENT:DEPARTMENT:DELETE_MULTIPLE')")
  @Transactional
  public Result deleteDepartmentMultiple(@RequestBody List<Integer> ids) {
    departmentService.removeBatchByIds(ids);

    UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
    updateWrapper.in("department_id", ids);
    updateWrapper.set("department_id", 1);

    employeeService.update(updateWrapper);

    return ResultUtil.success(null, "删除部门信息成功");
  }

//  不分页获取所有员工信息
  @GetMapping
  public Result getEmployeeMultiple() {
    List<Department> department = departmentService.list();
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("department", department);

    return ResultUtil.success(data, "获取部门信息成功");
  }

  @GetMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('MANAGEMENT:DEPARTMENT:GET_MULTIPLE')")
  public Result getDepartmentMultiple(@PathVariable(value = "id") Integer id) {
    Department department = departmentService.getById(id);
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("department", department);

    return ResultUtil.success(data, "获取部门信息成功");
  }

  @GetMapping(value = "/{current}/{size}")
  @PreAuthorize("hasAuthority('MANAGEMENT:DEPARTMENT:GET_MULTIPLE')")
  public Result getDepartmentMultiple(@PathVariable(value = "current") Integer current, @PathVariable(value = "size") Integer size) {
    IPage<Department> department = departmentService.page(new Page<>(current, size));
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("departmentPage", department);

    return ResultUtil.success(data, "获取部门信息成功");
  }

  @GetMapping(value = "/{current}/{size}/{name}")
  @PreAuthorize("hasAuthority('MANAGEMENT:DEPARTMENT:GET_MULTIPLE')")
  public Result getDepartmentMultiple(@PathVariable(value = "current") Integer current, @PathVariable(value = "size") Integer size,  @PathVariable(value = "name") String name) {
    QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<>();

    departmentQueryWrapper.like("name", '%' + name + '%');
    IPage<Department> department = departmentService.page(new Page<>(current, size), departmentQueryWrapper);
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("departmentPage", department);

    return ResultUtil.success(data, "获取部门信息成功");
  }

  @PutMapping
  @PreAuthorize("hasAuthority('MANAGEMENT:DEPARTMENT:UPDATE_SINGLE')")
  public Result putDepartment(@RequestBody Department department) {
    departmentService.updateById(department);

    return ResultUtil.success(null, "修改部门信息成功");
  }

  @GetMapping(value = "/test")
  public Result test() throws InterruptedException {
    Thread.sleep(50000);
    ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap<>();

    concurrentHashMap.put("message", "草泥马");

    return ResultUtil.success(concurrentHashMap,"草泥马2");
  }
}
