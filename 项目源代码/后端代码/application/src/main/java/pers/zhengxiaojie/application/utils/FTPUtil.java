package pers.zhengxiaojie.application.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FTPUtil {

  //ftp服务器ip地址
//  @Value(value = "${ftp.server}")
  private static String FTP_ADDRESS = "192.168.200.205";

//  @Value(value = "${ftp.port}")
  private static int FTP_PORT = 21;

//  @Value(value = "${ftp.user}")
  private static String FTP_USERNAME = "root";

//  @Value(value = "${ftp.password}")
  private static String FTP_PASSWORD = "root";

//  @Value(value = "${ftp.home}")
  public static String FTP_BASEPATH ="/usr/images";

  public boolean uploadFile(String originFileName, InputStream input) {
    System.out.println(FTPUtil.FTP_ADDRESS);
    System.out.println(FTPUtil.FTP_ADDRESS);
    System.out.println(FTPUtil.FTP_ADDRESS);
    System.out.println(FTPUtil.FTP_ADDRESS);
    System.out.println(FTPUtil.FTP_ADDRESS);
    boolean success = false;
    FTPClient ftp = new FTPClient();
    ftp.setControlEncoding("GBK");
    try {
      int reply;
      ftp.connect(FTP_ADDRESS, FTP_PORT);// 连接FTP服务器
      ftp.login(FTP_USERNAME, FTP_PASSWORD);// 登录
      reply = ftp.getReplyCode();
      if (!FTPReply.isPositiveCompletion(reply)) {
        ftp.disconnect();
        return success;
      }
      ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
      ftp.makeDirectory(FTP_BASEPATH);
      ftp.changeWorkingDirectory(FTP_BASEPATH);
      ftp.storeFile(originFileName, input);
      input.close();
      ftp.logout();
      success = true;
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (ftp.isConnected()) {
        try {
          ftp.disconnect();
        } catch (IOException ioe) {
        }
      }
    }
    return success;
  }
}
