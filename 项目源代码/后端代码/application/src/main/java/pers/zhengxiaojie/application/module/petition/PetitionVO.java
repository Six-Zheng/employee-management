package pers.zhengxiaojie.application.module.petition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
* 展示给前端的请假条信息
* */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetitionVO {
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
   * 此处表示申请的员工ID
   * */
  private Integer proposerAccount;
  /*
   * 此处表示处理的员工ID
   * */
  private Integer handlerAccount;
  /*
  * 此处表示申请的员工姓名
  * */
  private String proposerName;
  /*
  * 此处表示处理的员工姓名
  * */
  private String handlerName;


  private Integer id;

  private int isDeleted;

  private Integer version;

  private LocalDateTime createTime;

  private LocalDateTime updateTime;

  /*
   * 此处表示员工请假的开始时间
   * */
  private LocalDateTime startDate;
  /*
   * 此处表示员工请假的结束时间
   * */
  private LocalDateTime endDate;
}
