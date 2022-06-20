package pers.zhengxiaojie.application.module.email;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import pers.zhengxiaojie.application.exception.NoEmailException;
import pers.zhengxiaojie.application.module.employee.Employee;
import pers.zhengxiaojie.application.module.employee.EmployeeService;
import pers.zhengxiaojie.application.module.result.Result;
import pers.zhengxiaojie.application.utils.RedisUtil;
import pers.zhengxiaojie.application.utils.ResultUtil;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/email")
public class EmailController {
  @Autowired
  private JavaMailSender javaMailSender;

  @Value(value = "${spring.mail.username}")
  private String username;

  @Value(value = "${spring.mail.nickname}")
  private String nickname;

  @Autowired
  private FreeMarkerConfigurer freeMarkerConfigurer;

  @Autowired
  private RedisUtil redisUtil;

  @Autowired
  private EmployeeService employeeService;

//  发送随机4位验证码到用户邮箱
  @PostMapping(value = "/reset_password/{username1}")
  public Result sendResetPasswordVerificationCode(@PathVariable(value = "username1") String username1) throws IOException, TemplateException, MessagingException {
    Employee employee = employeeService.getEmployeeByUsername(username1);
    String email = employee.getEmail();
//    若用户无邮箱，则抛出异常
    if (StrUtil.isBlankOrUndefined(email)) {
      throw new NoEmailException("用户未绑定邮箱，请联系管理员重置密码");
    }

    String verificationCode = RandomUtil.randomString(4);
    Template template = freeMarkerConfigurer.getConfiguration().getTemplate("verificationCode.html");
    Map<String, Object> data = MapUtil.createMap(ConcurrentHashMap.class);

    int length = employee.getUsername().length();
    String emailUsername = employee.getUsername().substring(0,2);

    for (int i = 0; i < length -4; i++) {
      emailUsername += "*";
    }

    data.put("verificationCode", verificationCode);
    data.put("username", emailUsername + employee.getUsername().substring(length - 2, length));
    data.put("date", DateUtil.format(new Date(), "yyyy年MM月dd日"));

    String templateHtml = FreeMarkerTemplateUtils.processTemplateIntoString(template, data);
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

    mimeMessageHelper.setFrom(username, nickname);
    mimeMessageHelper.setTo(email);
    mimeMessageHelper.setSubject("重置密码");
    mimeMessageHelper.setText(templateHtml, true);
    redisUtil.setKeyAndValue("reset_password-" + employee.getUsername(), verificationCode, TimeUnit.SECONDS, 60);
    javaMailSender.send(mimeMessage);

    return ResultUtil.success(null,"发送邮件成功");
  }
}
