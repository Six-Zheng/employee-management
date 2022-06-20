package pers.zhengxiaojie.application.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

import java.util.Random;

public class MD5Util {
//    @Method 获取特定长度的随机字符串
//    @Parameter
//    length 长度
    public static String getRandomString(int length) {
        String randomStringData = "abcdefghijklmnopqrstuvwsyzABCDEFGHIJKLMNOPQRSTUVWXYZ,./;'[]!@#$%^&*()_+-=`~\\";
        StringBuilder randomstringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            randomstringBuilder.append(randomStringData.charAt(random.nextInt(randomStringData.length())));
        }

        String randomString = randomstringBuilder.toString();

        return randomString;
    }

//    @Method 获取经过盐与密码混合后生成的MD5值
//    @Parameter
//    password 密码
//    salt 盐
    public static String getMD5(String password, String salt, int interations) {
        Md5Hash md5Hash = new Md5Hash(password, salt, interations);

        return md5Hash.toString();
    }


//    @Method 校验经过盐与密码混合后生成的MD5值与给定的MD5值是否一致
//    @Parameter
//    password 密码
//    salt 盐
//    interations 迭代次数
    public static boolean verifyMD5IsValid(String md5, String password, String salt, int interations) {
        String md5HashResult = getMD5(password, salt, interations);

        if(md5.equals(md5HashResult))
            return true;

        return false;
    }
}
