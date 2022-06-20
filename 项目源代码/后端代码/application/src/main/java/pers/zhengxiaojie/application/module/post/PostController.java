package pers.zhengxiaojie.application.module.post;

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
@RequestMapping(value = "/post")
public class PostController {
  @Autowired
  private PostService postService;

  @Autowired
  private EmployeeService employeeService;

  @PostMapping
  @PreAuthorize("hasAuthority('MANAGEMENT:POST:POST_SINGLE')")
  public Result postPost(@RequestBody Post post) {
    postService.save(post);

    return ResultUtil.success(null, "添加岗位信息成功");
  }

  @DeleteMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('MANAGEMENT:POST:DELETE_SINGLE')")
  public Result deletePostSingle(@PathVariable(value = "id") Integer id) {
    postService.removeById(id);

    UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
    updateWrapper.eq("post_id", id);
    updateWrapper.set("post_id", 1);

    employeeService.update(updateWrapper);

    return ResultUtil.success(null, "删除岗位信息成功");
  }

  @DeleteMapping
  @PreAuthorize("hasAuthority('MANAGEMENT:POST:DELETE_MULTIPLE')")
  @Transactional
  public Result deletePostMultiple(@RequestBody List<Integer> ids) {
    postService.removeBatchByIds(ids);

    UpdateWrapper<Employee> updateWrapper = new UpdateWrapper<>();
    updateWrapper.in("post_id", ids);
    updateWrapper.set("post_id", 1);

    employeeService.update(updateWrapper);

    return ResultUtil.success(null, "删除岗位信息成功");
  }

  //  不分页获取所有员工信息
  @GetMapping
  public Result getPostMultiple() {
    List<Post> post = postService.list();
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("post", post);

    return ResultUtil.success(data, "获取岗位信息成功");
  }

  @GetMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('MANAGEMENT:POST:GET_MULTIPLE')")
  public Result getPostMultiple(@PathVariable(value = "id") Integer id) {
    Post post = postService.getById(id);
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("post", post);

    return ResultUtil.success(data, "获取岗位信息成功");
  }

  @GetMapping(value = "/{current}/{size}")
  @PreAuthorize("hasAuthority('MANAGEMENT:POST:GET_MULTIPLE')")
  public Result getPostMultiple(@PathVariable(value = "current") Integer current, @PathVariable(value = "size") Integer size) {
    IPage<Post> post = postService.page(new Page<>(current, size));
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("postPage", post);

    return ResultUtil.success(data, "获取岗位信息成功");
  }

  @GetMapping(value = "/{current}/{size}/{name}")
  @PreAuthorize("hasAuthority('MANAGEMENT:POST:GET_MULTIPLE')")
  public Result getPostMultiple(@PathVariable(value = "current") Integer current, @PathVariable(value = "size") Integer size,  @PathVariable(value = "name") String name) {
    QueryWrapper<Post> postQueryWrapper = new QueryWrapper<>();

    postQueryWrapper.like("name", '%' + name + '%');
    IPage<Post> post = postService.page(new Page<>(current, size), postQueryWrapper);
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("postPage", post);

    return ResultUtil.success(data, "获取岗位信息成功");
  }

  @PutMapping
  @PreAuthorize("hasAuthority('MANAGEMENT:POST:UPDATE_SINGLE')")
  public Result putPost(@RequestBody Post post) {
    postService.updateById(post);

    return ResultUtil.success(null, "修改岗位信息成功");
  }
}
