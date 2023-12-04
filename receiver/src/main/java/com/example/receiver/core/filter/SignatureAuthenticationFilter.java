package com.example.receiver.core.filter;


import com.example.receiver.core.handler.SignatureHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignatureAuthenticationFilter extends OncePerRequestFilter {

  private static final String KEY_ID = "key-id";
  private static final String SIGNATURE = "signature";
  private final SignatureHandler signatureHandler;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    HttpServletRequest requestWrapper = new ContentCachingRequestWrapper(request);
    String keyId = request.getHeader(KEY_ID);
    String signature = request.getHeader(SIGNATURE);

    filterChain.doFilter(requestWrapper, response);

    String body = getRequestBody(requestWrapper);

    if (verifiedSignature(keyId, signature, body)) {
      log.info("Signature verified Good");
      Authentication auth = new UsernamePasswordAuthenticationToken("user", null, AuthorityUtils.NO_AUTHORITIES);
      SecurityContextHolder.getContext().setAuthentication(auth);
    } else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Invalid signature");
    }
  }

  private String getRequestBody(HttpServletRequest request) {
    ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
    if (wrapper != null) {
      byte[] buf = wrapper.getContentAsByteArray();
      if (buf.length > 0) {
        try {
          return new String(buf, wrapper.getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
          return " - ";
        }
      }
    }
    return " - ";
  }

  private boolean verifiedSignature(String keyId, String signature, String body) {
    return signatureHandler.verify(keyId, signature, body);
  }

}
