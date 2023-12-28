package com.example.provider.application.echo.service;

import com.example.provider.application.echo.controller.EchoMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EchoServiceTest {
	
	@Autowired
	private EchoService echoService;
	
	@Test
	void testCase1() {
		EchoMessage echoMessage = new EchoMessage();
		echoMessage.set("keyId", "20231205");
		
	}
}