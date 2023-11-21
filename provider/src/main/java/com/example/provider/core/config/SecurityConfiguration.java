package com.example.provider.core.config;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Configuration;

import java.security.Security;

@Configuration
public class SecurityConfiguration {


  public SecurityConfiguration() {
    Security.addProvider(new BouncyCastleProvider());
  }
}
