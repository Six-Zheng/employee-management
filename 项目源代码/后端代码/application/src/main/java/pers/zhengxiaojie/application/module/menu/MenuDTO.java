package pers.zhengxiaojie.application.module.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {
  private Integer id;

  private String title;

  private String path;

  private String component;

  private Integer parentId;

  private Integer orderNum;

  private Integer level;

  private String tag;

  private Integer status;

  private List<MenuDTO> children;
}
