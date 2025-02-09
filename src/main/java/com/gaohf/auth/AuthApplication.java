package com.gaohf.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({"com.example.security.entity"})  // 扫描实体类
@ComponentScan({"com.gaohf.auth", "com.example.security"})  // 扫描组件
@EnableJpaRepositories({"com.example.security.repository"})  // 扫描Repository
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

}
