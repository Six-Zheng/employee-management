package pers.zhengxiaojie.application.module.post;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.zhengxiaojie.application.module.entity.Entity;

@TableName(value = "post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post extends Entity {
  private String name;

  private String description;
}
