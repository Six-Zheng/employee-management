package pers.zhengxiaojie.application.module.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entity implements Serializable {
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  @TableField(value = "is_deleted")
  @TableLogic(value = "0", delval = "1")
  private int isDeleted;

  @Version
  @TableField(value = "version", fill = FieldFill.INSERT_UPDATE)
  private Integer version;

  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
  private LocalDateTime updateTime;
}
