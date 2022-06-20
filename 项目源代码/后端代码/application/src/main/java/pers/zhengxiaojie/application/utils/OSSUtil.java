package pers.zhengxiaojie.application.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class OSSUtil {
  private static final String END_POINT = "https://oss-cn-guangzhou.aliyuncs.com";
  private static final String ACCESS_KEY_ID = "LTAI4GF8Eg69ckwhuv2cYaLn";
  private static final String ACCESS_KEY_SECRET = "QMrakmG2mBuycNmSV6DTg8IftFSSwN";
  private static final String BUCKET_NAME = "eduteacher-pic";

  public static void uploadImage(MultipartFile file, String username) {
    OSS ossClient = new OSSClient(END_POINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

    try {
      // 创建存储空间。
      ossClient.createBucket(BUCKET_NAME);
      ossClient.putObject(BUCKET_NAME, username + ".jpg", file.getInputStream());

    } catch (OSSException oe) {
      System.out.println("Caught an OSSException, which means your request made it to OSS, "
        + "but was rejected with an error response for some reason.");
      System.out.println("Error Message:" + oe.getErrorMessage());
      System.out.println("Error Code:" + oe.getErrorCode());
      System.out.println("Request ID:" + oe.getRequestId());
      System.out.println("Host ID:" + oe.getHostId());
    } catch (ClientException ce) {
      System.out.println("Caught an ClientException, which means the client encountered "
        + "a serious internal problem while trying to communicate with OSS, "
        + "such as not being able to access the network.");
      System.out.println("Error Message:" + ce.getMessage());
    } catch (IOException ioException) {
      ioException.printStackTrace();
    } finally {
      if (ossClient != null) {
        ossClient.shutdown();
      }
    }
  }

}
