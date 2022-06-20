package pers.zhengxiaojie.application.extension;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

//自定义HttpServletRequestWrapper，防止HttpServletRequest只能调用一次getInputStream
public class CustomizeHttpServletRequestWrapper extends HttpServletRequestWrapper {

  private byte[] requestBody = null; // 用于将流保存下来

  public CustomizeHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
    super(request);
    requestBody = StreamUtils.copyToByteArray(request.getInputStream());
  }
  @Override
  public ServletInputStream getInputStream() {
    final ByteArrayInputStream bais = new ByteArrayInputStream(requestBody);
    return new ServletInputStream() {
      @Override
      public int read(){
        return bais.read();  // 读取 requestBody 中的数据
      }
      @Override
      public boolean isFinished() {
        return false;
      }
      @Override
      public boolean isReady() {
        return false;
      }
      @Override
      public void setReadListener(ReadListener readListener) { }
    };
  }
  @Override
  public BufferedReader getReader() throws IOException {
    return new BufferedReader(new InputStreamReader(getInputStream()));
  }
}