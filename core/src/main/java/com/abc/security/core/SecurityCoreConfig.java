package com.abc.security.core;

import com.abc.security.core.properties.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
//让spring读取配置
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityCoreConfig {
}
