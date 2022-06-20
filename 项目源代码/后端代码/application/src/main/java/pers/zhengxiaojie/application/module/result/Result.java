package pers.zhengxiaojie.application.module.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
  private Integer code; // 请求码

  private ConcurrentHashMap<String, Object> data; // 成功响应数据

  private String message; // 操作信息
}
