package com.example.receiver.core.filter;

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

    logDivider();
    logRequest(requestWrapper);

    chain.doFilter(httpServletRequest, response);

    logDivider();
    logResponse(response);
    logDivider();
  }

  private void logRequest(ContentCachingRequestWrapper requestWrapper) {
    logHeaders(requestWrapper.getHeaderNames(), requestWrapper);
    log.info("Request URI: {}", requestWrapper.getRequestURI());
    log.info("Request Method: {}", requestWrapper.getMethod());
    log.info("Request Body: {}", new String(requestWrapper.getContentAsByteArray()));
  }

  private void logHeaders(Enumeration<String> headerNames, HttpServletRequest request) {
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      String headerValue = request.getHeader(headerName);
      log.info("Header {}: {}", headerName, headerValue);
    }
  }

  private void logResponse(ServletResponse response) {
    log.info("Response ContentType: {}", response.getContentType());
    log.info("Response CharacterSet: {}", response.getCharacterEncoding());
    log.info("Response Body: {}", response);
  }

  private void logDivider() {
    log.info("--------------------------------------------------");
  }
}
