package pers.zhengxiaojie.application.module.data;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.zhengxiaojie.application.module.department.Department;
import pers.zhengxiaojie.application.module.department.DepartmentService;
import pers.zhengxiaojie.application.module.employee.Employee;
import pers.zhengxiaojie.application.module.employee.EmployeeService;
import pers.zhengxiaojie.application.module.post.Post;
import pers.zhengxiaojie.application.module.post.PostService;
import pers.zhengxiaojie.application.module.result.Result;
import pers.zhengxiaojie.application.utils.ResultUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(value = "/data")
public class DataController {
  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private DepartmentService departmentService;

  @Autowired
  private PostService postService;

  @GetMapping
  public Result getData() {
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    /**
     * 封装男生、女生以及总人数数据
     */
    long count = employeeService.count(); // 获取总人数
    long maleCount = employeeService.count(new QueryWrapper<Employee>().eq("sex", 1)); // 获取男生人数
    long femaleCount = count - maleCount; // 获取女生人数
    ConcurrentHashMap<String, Object> counts = new ConcurrentHashMap<>();
    counts.put("count", count);
    counts.put("maleCount", maleCount);
    counts.put("femaleCount", femaleCount);

    /**
     * 获取所有部门人数数据
     */
    long departmentCount = departmentService.count(); // 获取部门个数
    List<Department> departmentList = departmentService.list(); // 获取部门清单
    ConcurrentHashMap<String, Object> departmentCounts = new ConcurrentHashMap<>();
    departmentCounts.put("departmentCount", departmentCount);
    ArrayList<String> departmentName = new ArrayList<>();
    ArrayList<Integer> departmentValue = new ArrayList<>();

    departmentList.forEach(item -> {
      departmentName.add(new String(item.getName()));
      departmentValue.add((int) employeeService.count(new QueryWrapper<Employee>().eq("department_id", item.getId())));
    });
    departmentCounts.put("departmentName", departmentName);
    departmentCounts.put("departmentValue", departmentValue);

    /**
     * 获取所有岗位人数数据
     */
    long postCount = postService.count(); // 获取部门个数
    List<Post> postList = postService.list(); // 获取部门清单
    ConcurrentHashMap<String, Object> postCounts = new ConcurrentHashMap<>();
    postCounts.put("postCount", postCount);
    ArrayList<String> postName = new ArrayList<>();
    ArrayList<Integer> postValue = new ArrayList<>();

    postList.forEach(item -> {
      postName.add(new String(item.getName()));
      postValue.add((int) employeeService.count(new QueryWrapper<Employee>().eq("post_id", item.getId())));
    });
    postCounts.put("postName", postName);
    postCounts.put("postValue", postValue);


    data.put("counts", counts);
    data.put("departmentCounts", departmentCounts);
    data.put("postCounts", postCounts);
    return ResultUtil.success(data, "获取数据成功");
  }
}
