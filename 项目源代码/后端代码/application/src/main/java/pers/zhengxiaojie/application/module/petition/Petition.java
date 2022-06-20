package pers.zhengxiaojie.application.module.petition;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.zhengxiaojie.application.module.entity.Entity;

import java.time.LocalDateTime;

@TableName(value = "petition")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Petition extends Entity {
  /*
  * 此处表示请假内容
  * */
  private String content;
  /*
  * 此处表示回复内容
  * */
  private String reply;
  /*
  * 此处表示请假类型
  * */
  private Integer type;
  /*
  * 此处表示请假处理状态
  * */
  private Integer isHandled;
  /*
  * 此处表示申请的员工Account
  * */
  private Integer proposerAccount;
  /*
  * 此处表示处理的员工Account
  * */
  private Integer handlerAccount;
  /*
  * 此处表示员工请假的开始时间
  * */
  private LocalDateTime startDate;
  /*
  * 此处表示员工请假的结束时间
  * */
  private LocalDateTime endDate;
}
