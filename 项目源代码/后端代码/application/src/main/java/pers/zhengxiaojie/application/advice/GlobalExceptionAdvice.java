package pers.zhengxiaojie.application.advice;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.zhengxiaojie.application.module.result.Result;
import pers.zhengxiaojie.application.utils.ResultUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpURLConnection;
import java.util.concurrent.ConcurrentHashMap;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionAdvice {
  @ExceptionHandler({Exception.class})
  public Result exceptionHandler(Exception exception, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    httpServletResponse.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    Result result = ResultUtil.failure(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, exception.getMessage());

    return result;
  }

  @ExceptionHandler(value = AccessDeniedException.class)
  public Result accessNo(Exception exception, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    httpServletResponse.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    Result result = ResultUtil.failure(HttpStatus.FORBIDDEN.value(), null, "当前用户无此访问权限");

    return result;
  }
}
