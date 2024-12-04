package com.catface_task;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.catface_task.common.mapper")
public class CatfaceTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatfaceTaskApplication.class, args);
	}

}
