package com.dip.unifiedviewer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class UnifiedViewerServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(UnifiedViewerServiceApplication.class, args);
	}
}
