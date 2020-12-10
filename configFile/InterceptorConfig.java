package com.fa.ebs.config;

import com.fa.ebs.dev.service.DevFaultTimeService;
import com.fa.ebs.dev.service.DevInfoService;
import com.fa.ebs.interceptor.DevConnInterceptor;
import com.fa.redis.RedisValidateCodeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	@Autowired
	private DevFaultTimeService devFaultTimeService;

	@Autowired
	private DevInfoService devInfoService;

	@Autowired
	private RedisValidateCodeManager redisValidateCodeManager;

	@Autowired
	private RedisTemplate redisTemplate;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(
				new DevConnInterceptor(devInfoService, devFaultTimeService, redisValidateCodeManager, redisTemplate));
	}
}
