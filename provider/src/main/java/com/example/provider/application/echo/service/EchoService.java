package com.example.provider.application.echo.service;

import com.example.provider.application.echo.controller.EchoMessage;
import com.example.provider.common.helper.JsonHelper;
import com.example.provider.common.helper.RestTemplateHelper;
import com.example.provider.common.helper.SignatureHelper;
import com.example.provider.common.utils.FileUtils;
import com.example.provider.core.props.ExternalUrlProperties;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class EchoService {
	
	private final ExternalUrlProperties externalUrlProperties;
	private final RestTemplateHelper restTemplateHelper;
	private final JsonHelper jsonHelper;
	
	public EchoMessage echoMessage(EchoMessage echoMessage) {
		String requestBody = jsonHelper.toJson(echoMessage);
		
		try {
			// 요청 Body에 대한 Signature 생성
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String keyId = StringUtils.isNotBlank(echoMessage.getKeyId()) ? echoMessage.getKeyId() : dateFormat.format(new Date());
			
			Resource privateKeyResource = FileUtils.readFileAsResource(keyId, "privateKey.pem");
			if (privateKeyResource != null && privateKeyResource.isReadable()) {
				
				PrivateKey privateKey = SignatureHelper.readPrivateByPem(privateKeyResource);
				
				byte[] payload = requestBody.getBytes(StandardCharsets.UTF_8);
				byte[] signature = SignatureHelper.createSignature(privateKey, payload);
				
				log.info("keyId : {}", keyId);
				
				ResponseEntity<EchoMessage> echoResponse = restTemplateHelper.postRestTemplate(
								externalUrlProperties.getEcho(),
								makeHeaders(signature, keyId),
								requestBody,
								EchoMessage.class
				);
				if (!echoResponse.getStatusCode().is2xxSuccessful()) {
					log.error("Echo API 호출 실패");
					throw new RuntimeException("Echo API 호출 실패");
				}
				log.info("Echo API 호출 성공");
				return echoMessage;
			}
			
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			log.error("HTTP error occurred: {}", e.getStatusCode());
			throw new RuntimeException("HTTP error occurred", e);
		} catch (ResourceAccessException e) {
			log.error("Network error occurred: {}", e.getMessage());
			throw new RuntimeException("Network error occurred", e);
		}
		throw new RuntimeException("Echo API 호출 실패");
	}
	
	private static MultiValueMap<String, String> makeHeaders(byte[] signature, String keyId) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
		header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		header.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		header.add(HttpHeaders.CONTENT_ENCODING, StandardCharsets.UTF_8.name());
		
		String value = Base64.getEncoder().encodeToString(signature);
		log.info("keyId :: {}, signature :: {}", keyId, value);
		header.add("signature", value);
		header.add("key-id", keyId);
		return header;
	}
}
