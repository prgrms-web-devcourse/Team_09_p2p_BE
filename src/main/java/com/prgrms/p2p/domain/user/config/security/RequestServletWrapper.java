package com.prgrms.p2p.domain.user.config.security;

import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RequestServletWrapper extends HttpServletRequestWrapper {

  private static final Logger logger = LoggerFactory.getLogger(RequestServletWrapper.class);
  private String requestData = null;

  public RequestServletWrapper(HttpServletRequest request) {
    super(request);
    try (Scanner s = new Scanner(request.getInputStream()).useDelimiter("\\A")) {
      requestData = s.hasNext() ? s.next() : "";
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public ServletInputStream getInputStream() throws IOException {

    StringReader reader = new StringReader(requestData);
    return new ServletInputStream() {
      private ReadListener readListener = null;
      @Override
      public int read() throws IOException {
        return reader.read();
      }

      @Override
      public void setReadListener(ReadListener listener) {
        this.readListener = listener;
        try {
          if (!isFinished()) {
            readListener.onDataAvailable();
          } else {
            readListener.onAllDataRead();
          }
        } catch (IOException io) {
          logger.info("RequestServlet Wrapper - setReadListener 에러");
        }
      }

      @Override
      public boolean isReady() {
        return isFinished();
      }

      @Override
      public boolean isFinished() {
        try {
          return reader.read() < 0;
        } catch (IOException e) {
          logger.info("RequestServlet Wrapper - isFinished 에러");
        }
        return false;
      }
    };
  }
}
