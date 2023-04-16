//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.service;

import cn.edu.xmu.javaee.core.jpa.SelectiveUpdateJpaRepositoryImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.javaee.core",
		"cn.edu.xmu.oomall.service"})
@EnableJpaRepositories(value = "cn.edu.xmu.javaee.core.jpa", repositoryBaseClass = SelectiveUpdateJpaRepositoryImpl.class, basePackages = "cn.edu.xmu.oomall.service.mapper")
@EnableMongoRepositories(basePackages = "cn.edu.xmu.oomall.service.mapper")
@EnableDiscoveryClient
public class ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}
}
