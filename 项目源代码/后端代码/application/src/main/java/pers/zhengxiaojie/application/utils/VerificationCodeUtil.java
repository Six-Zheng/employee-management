package pers.zhengxiaojie.application.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class VerificationCodeUtil {
  private static final List<BackgroundColorAndFontColor> backgroundColorAndFontColorList;

  private static final Properties properties;

  private static final List<String> verificationCodeStyleList;

  static {
//     初始化所有颜色
    String backGroundColor =
        "236,204,104-"+
        "255,127,80-"+
        "255,165,2-"+
        "255,99,72-"+
        "123,237,159-"+
        "112,161,255-"+
        "30,144,255-"+
        "46,213,115";
    String fontColor =
        "255,255,255-"+
        "255,255,255-"+
        "255,255,255-"+
        "255,255,255-"+
        "255,255,255-"+
        "255,255,255-"+
        "255,255,255-"+
        "255,255,255";
    String[] backGroundColorArray = backGroundColor.split("-");
    String[] fontColorArray = fontColor.split("-");

    backgroundColorAndFontColorList = new ArrayList<>();

    for (int i = 0; i < backGroundColorArray.length; i++) {
      backgroundColorAndFontColorList.add(new BackgroundColorAndFontColor(backGroundColorArray[i], fontColorArray[i]));
    }

//    初始化所有验证码图片样式
    verificationCodeStyleList = new ArrayList<>();
    verificationCodeStyleList.add("com.google.code.kaptcha.impl.WaterRipple");
    verificationCodeStyleList.add("com.google.code.kaptcha.impl.ShadowGimpy");

//    初始化验证码配置
    properties = new Properties();
    properties.setProperty("kaptcha.border", "no");
    properties.setProperty("kaptcha.image.width", "90");
    properties.setProperty("kaptcha.image.height", "40");
    properties.setProperty("kaptcha.textproducer.impl", "com.google.code.kaptcha.text.impl.DefaultTextCreator");
    properties.setProperty("kaptcha.textproducer.char.length", "4");
    properties.setProperty("kaptcha.textproducer.font.names", "微软雅黑");
    properties.setProperty("kaptcha.textproducer.font.size", "30");
    properties.setProperty("kaptcha.textproducer.font.color", getRandomRGB().get("fontColor"));
    properties.setProperty("kaptcha.textproducer.char.string", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
    properties.setProperty("kaptcha.textproducer.char.space", "3");
    properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
    properties.setProperty("kaptcha.obscurificator.impl", getRandomVerificationCodeStyle());
    properties.setProperty("kaptcha.background.impl", "com.google.code.kaptcha.impl.DefaultBackground");
    properties.setProperty("kaptcha.background.clear.to", getRandomRGB().get("backgroundColor"));
    properties.setProperty("kaptcha.background.clear.form", getRandomRGB().get("backgroundColor"));
    properties.setProperty("kaptcha.word.impl", "com.google.code.kaptcha.text.impl.DefaultWordRenderer");
  }

//  获取随机的前景色和背景色的搭配
  public static ConcurrentHashMap<String, String> getRandomRGB() {
    BackgroundColorAndFontColor backgroundColorAndFontColor = RandomUtil.randomEle(backgroundColorAndFontColorList);

    return (ConcurrentHashMap<String, String>) MapUtil.builder(new ConcurrentHashMap<String, String>())
      .put("backgroundColor", backgroundColorAndFontColor.getBackgroundColor())
      .put("fontColor", backgroundColorAndFontColor.getFontColor()).build();
  }

//  获取验证码图片样式
  public static String getRandomVerificationCodeStyle() {
    return RandomUtil.randomEle(verificationCodeStyleList);
  }

//  获取验证码的Base64编码
  public static String getVerificationCodeToBase64(String verificationCode) throws IOException {
    DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
    Config config = new Config(properties);

    defaultKaptcha.setConfig(config);

    BufferedImage image = defaultKaptcha.createImage(verificationCode);
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(image, "png", byteArrayOutputStream);
    Base64.Encoder encoder = Base64.getEncoder();

    return "data:image/jpeg;base64," + encoder.encodeToString(byteArrayOutputStream.toByteArray());
  }

  public static void main(String[] args) throws IOException {
    System.out.println(getVerificationCodeToBase64(RandomUtil.randomString(4)));
  }
}

class BackgroundColorAndFontColor {
  private String backgroundColor;

  private String fontColor;

  public BackgroundColorAndFontColor() {
  }

  public BackgroundColorAndFontColor(String backgroundColor, String fontColor) {
    this.backgroundColor = backgroundColor;
    this.fontColor = fontColor;
  }

  public String getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public String getFontColor() {
    return fontColor;
  }

  public void setFontColor(String fontColor) {
    this.fontColor = fontColor;
  }
}


