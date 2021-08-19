package com.konkuk.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
public class SampleApplication {
	public static void main(String[] args) {
		SpringApplication.run(SampleApplication.class, args);
	}
}

/**
 온전히 잘 작동되는 1번째 버전입니다.
 추후 기능을 확장하거나 리팩터링, 안정화, 최적화할 것을 대비해
 이렇게 안정적인 버전을 기록해놓습니다.
 */
