package pers.zhengxiaojie.application.module.verificationCode;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.zhengxiaojie.application.module.result.Result;
import pers.zhengxiaojie.application.utils.RedisUtil;
import pers.zhengxiaojie.application.utils.ResultUtil;
import pers.zhengxiaojie.application.utils.VerificationCodeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/verification_code")
public class VerificationCodeController {
  @Autowired
  private RedisUtil redisUtil;

  @GetMapping
  private Result getVerificationCode(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
    String verificationCode = RandomUtil.randomString(4);
    String verificationCodeKey = UUID.randomUUID().toString();
    String verificationCodeImage = VerificationCodeUtil.getVerificationCodeToBase64(verificationCode);

    redisUtil.setKeyAndValue("login-" + verificationCodeKey, verificationCode, TimeUnit.MINUTES, 30);

    ConcurrentHashMap<String, Object> data = new ConcurrentHashMap<>();

    data.put("verificationCodeKey", verificationCodeKey);
    data.put("verificationCodeImage", verificationCodeImage);
    data.put("verificationCode", verificationCode);

    return ResultUtil.success(data, "获取验证码成功");
  }
}
