package pers.zhengxiaojie.application.module.employee;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
  @Autowired
  private EmployeeMapper employeeMapper;

  @Override
  public Employee getEmployeeByUsername(String username) {
    QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();

    employeeQueryWrapper.lambda().eq(Employee::getUsername, username);

    Employee employee = employeeMapper.selectOne(employeeQueryWrapper);

    return employee;
  }
}
