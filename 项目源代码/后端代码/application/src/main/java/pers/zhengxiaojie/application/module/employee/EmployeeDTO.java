package pers.zhengxiaojie.application.module.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
  private String name;

  private String username;

  private String email;

  private String phone;

  private String sex;

  private String departmentId;

  private String postId;

  private String roleId;
}
