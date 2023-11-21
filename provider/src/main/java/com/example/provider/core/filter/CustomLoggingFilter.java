package com.example.provider.core.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

@Slf4j
public class CustomLoggingFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {

    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpServletRequest);

    logRequest(requestWrapper);

    chain.doFilter(requestWrapper, response);

    logResponse(response);
  }

  private void logRequest(ContentCachingRequestWrapper requestWrapper) {
    logheaders(requestWrapper.getHeaderNames());
    log.info("Request URI: {}", requestWrapper.getRequestURI());
    log.info("Request Method: {}", requestWrapper.getMethod());
    log.info("Request Body: {}", new String(requestWrapper.getContentAsByteArray()));
  }

  private void logheaders(Enumeration<String> headerNames) {
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      log.info("Header {} {} ", headerName, headerName);
    }
  }

  private void logResponse(ServletResponse response) {
    log.info("Response ContentType: {}", response.getContentType());
    log.info("Response CharacterSet: {}", response.getCharacterEncoding());
    log.info("Response Body: {}", response);
  }
}
