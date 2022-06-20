package pers.zhengxiaojie.application.module.employee;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.zhengxiaojie.application.module.entity.Entity;

@TableName(value = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends Entity {
  private String avatar;

  private String name;

  private String username;

  private String password;

  private String salt;

  private String email;

  private String phone;

  private Integer sex;

  private Integer departmentId;

  private Integer postId;

  private Integer roleId;
}
