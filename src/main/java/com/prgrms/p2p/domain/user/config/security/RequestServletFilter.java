package com.prgrms.p2p.domain.user.config.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestServletFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (request.getContentType() != null && request.getContentType().toLowerCase().contains("multipart/form-data")) {
      chain.doFilter(request, response);
    }
    else {
      RequestServletWrapper wrappedRequest = new RequestServletWrapper((HttpServletRequest) request);
      chain.doFilter(wrappedRequest, response);
    }
  }
}
