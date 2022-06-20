package pers.zhengxiaojie.application.module.role;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.zhengxiaojie.application.module.entity.Entity;

@TableName(value = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role extends Entity {
  private String name;

  private String description;

  private String tag;
}
