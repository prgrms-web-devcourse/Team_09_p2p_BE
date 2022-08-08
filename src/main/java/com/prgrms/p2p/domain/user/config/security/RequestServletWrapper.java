package com.prgrms.p2p.domain.user.config.security;

import com.amazonaws.util.IOUtils;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RequestServletWrapper extends HttpServletRequestWrapper {

  private static final Logger logger = LoggerFactory.getLogger(RequestServletWrapper.class);
  // 가로챈 데이터를 가공하여 담을 final 변수
  private final String requestBody;

  public RequestServletWrapper(HttpServletRequest request) throws IOException{
    super(request);

    String requestData = requestDataByte(request); // Request Data 가로채기
    requestBody = requestData;
  }

  @Override
  public ServletInputStream getInputStream() {
    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
    return new ServletInputStream() {
      @Override
      public boolean isFinished() {
        return false;
      }

      @Override
      public boolean isReady() {
        return false;
      }

      @Override
      public void setReadListener(ReadListener listener) {
      }

      @Override
      public int read() {
        return byteArrayInputStream.read();
      }
    };
  }

  @Override
  public BufferedReader getReader() {
    return new BufferedReader(new InputStreamReader(this.getInputStream()));
  }

  //==request Body 가로채기==//
  private String requestDataByte(HttpServletRequest request) throws IOException {
    byte[] rawData = new byte[128];
    InputStream inputStream = request.getInputStream();
    rawData = IOUtils.toByteArray(inputStream);
    return new String(rawData);
  }
}
