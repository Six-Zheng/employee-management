package pers.zhengxiaojie.application.module.department;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.zhengxiaojie.application.module.entity.Entity;

@TableName(value = "department")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department extends Entity {

  private String name;

  private String description;
}
