package pers.zhengxiaojie.application.module.role_menu;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.zhengxiaojie.application.module.entity.Entity;

@TableName(value = "role_menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenu extends Entity {
  private Integer roleId;

  private Integer menuId;

  @TableField(value = "status", fill = FieldFill.INSERT_UPDATE)
  private Integer status;
}
