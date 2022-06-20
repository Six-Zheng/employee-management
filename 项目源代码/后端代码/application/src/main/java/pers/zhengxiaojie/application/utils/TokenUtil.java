package pers.zhengxiaojie.application.utils;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;


public class TokenUtil {

//    @Method 获取令牌
//    @Parameter
//    payload 载荷
//    signature 签名
  public static String getToken(ConcurrentHashMap<String, Object> payload, String signature) {

    Calendar expire = Calendar.getInstance();

    expire.add(Calendar.MINUTE, 1);

    long date = new Date().getTime();
    long expireDate = date + 30 * 60 * 1000;

    String token =JWT.create().withPayload(payload).withExpiresAt(new Date(expireDate)).sign(Algorithm.HMAC256(signature));

    return token;
  }

//    @Method 获取令牌
//    @Parameter
//    expire 过期时间
//    payload 载荷
//    signature 签名
  public static String getToken(Calendar expire, ConcurrentHashMap<String, Object> payload, String signature) {
    long expireTime = expire.getTimeInMillis();
    long date = new Date().getTime();
//    long expireDate = date + 30 * 60 * 1000;
    long expireDate = date + 24 * 60 * 60 * 1000 * 24;

    String token = JWT.create().withPayload(payload).withExpiresAt(new Date(expireDate)).sign(Algorithm.HMAC256(signature));

    return token;
  }

//    @Method 验证令牌
//    @Parameter
//    token 令牌
//    signature 签名
  public static boolean verifyToken(String token, String secret) {

    JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();

    jwtVerifier.verify(token);

    return true;
  }

//    @Method 获取令牌负载数据
//    @Parameter
//    token 令牌
//    signature 签名
  public static ConcurrentHashMap<String, Object> getTokenPayloadData(String token, String signature) throws UnsupportedEncodingException, JsonProcessingException {

    boolean ifTokenIsValidated = verifyToken(token, signature);

    if (ifTokenIsValidated) {
      DecodedJWT decodeToken = JWT.decode(token);
      String payload = decodeToken.getPayload();

      String payloadString = new String(Base64.decodeBase64(payload), StandardCharsets.UTF_8);
      ConcurrentHashMap<String, Object> payloadData = JSON.parseObject(payloadString, ConcurrentHashMap.class);

      return payloadData;
    }

    return null;
  }


}

