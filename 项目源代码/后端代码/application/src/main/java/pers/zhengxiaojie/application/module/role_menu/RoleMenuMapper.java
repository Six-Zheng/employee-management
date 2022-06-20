package pers.zhengxiaojie.application.module.role_menu;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {
  public void recoveryById(@Param(value = "id") Integer id);

  List<RoleMenu> selectMenu(@Param(value = "roleId")Integer roleId);
}
