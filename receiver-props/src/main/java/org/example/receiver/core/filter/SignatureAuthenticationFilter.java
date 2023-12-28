package org.example.receiver.core.filter;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.receiver.core.handler.SignatureHandler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SignatureAuthenticationFilter extends OncePerRequestFilter {
	
	private static final String KEY_ID = "key-id";
	private static final String SIGNATURE = "signature";
	private final SignatureHandler signatureHandler;
	
	@Override
	protected void doFilterInternal(
					HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
	) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
		log.info("[FILTER][REQUEST] URI: {}", requestURI);
		
		CachedBodyHttpServletRequest requestWrapper = new CachedBodyHttpServletRequest(request);
		String keyId = requestWrapper.getHeader(KEY_ID);
		String signature = requestWrapper.getHeader(SIGNATURE);
		
		if (StringUtils.isBlank(keyId) || StringUtils.isBlank(signature)) {
			log.warn("[FILTER][UNAUTHORIZED] Missing keyId or signature for URI: {}", requestURI);
			respondUnauthorized(response, "Invalid signature");
			return;
		}
		
		if (signatureHandler.absent(keyId)) {
			log.warn("[FILTER][UNAUTHORIZED] Absent keyId: {} for URI: {}", keyId, requestURI);
			respondUnauthorized(response, "Absent keyId");
			return;
		}
		
		String body = requestWrapper.getCachedBodyAsString();
		if (!verifiedSignature(keyId, signature, body)) {
			log.warn("[FILTER][UNAUTHORIZED] Invalid signature for keyId: {} and URI: {}", keyId, requestURI);
			respondUnauthorized(response, "Invalid signature");
			return;
		}
		
		log.info("[FILTER][SUCCESS] Signature verified for keyId: {} and URI: {}", keyId, requestURI);
		Authentication auth = new UsernamePasswordAuthenticationToken("user", null, AuthorityUtils.NO_AUTHORITIES);
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		filterChain.doFilter(requestWrapper, response);
	}
	
	private boolean verifiedSignature(String keyId, String signature, String body) {
		boolean verify = signatureHandler.verify(keyId, signature, body);
		log.debug("[FILTER][VERIFY] KeyId: {}, Verify: {}", keyId, verify);
		return verify;
	}
	
	private void respondUnauthorized(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(message);
	}
}
