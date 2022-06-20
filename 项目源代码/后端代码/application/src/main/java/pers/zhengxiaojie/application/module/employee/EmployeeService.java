package pers.zhengxiaojie.application.module.employee;

import com.baomidou.mybatisplus.extension.service.IService;

public interface EmployeeService extends IService<Employee> {
  // 通过username获取Employee
  Employee getEmployeeByUsername(String username);
}
