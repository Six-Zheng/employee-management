package pers.zhengxiaojie.application.utils;

import org.springframework.http.HttpStatus;
import pers.zhengxiaojie.application.module.result.Result;

import java.util.concurrent.ConcurrentHashMap;

public class ResultUtil {

//  成功响应方法
  public static Result success(ConcurrentHashMap<String, Object> data, String message) {
    return new Result(HttpStatus.OK.value(), data, message);
  }

//  失败响应方法
  public static Result failure(Integer code, ConcurrentHashMap<String, Object> data, String message) {
    return new Result(code, data, message);
  }
}
