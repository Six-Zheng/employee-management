package pers.zhengxiaojie.application.module.menu;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.zhengxiaojie.application.module.entity.Entity;

@TableName(value = "menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu extends Entity {
  private String title;

  private String path;

  private String component;

  private Integer parentId;

  private Integer orderNum;

  private Integer level;

  private String tag;

  private Integer status;
}
