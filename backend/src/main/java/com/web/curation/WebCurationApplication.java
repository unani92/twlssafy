package com.web.curation;

// import com.web.curation.JWT.JwtInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class WebCurationApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(WebCurationApplication.class, args);
	}

	// @Autowired
	// private JwtInterceptor jwtInterceptor;

	// @Override
	// public void addInterceptors(InterceptorRegistry registry) {
	// 	registry.addInterceptor(jwtInterceptor).addPathPatterns("/admin/**")
	// 	.excludePathPatterns("/**");
	// }


	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
		.allowedOrigins("*")
		.allowedMethods("*")
		.allowedHeaders("*")
		.exposedHeaders("jwt-auth-token");
	}
}
