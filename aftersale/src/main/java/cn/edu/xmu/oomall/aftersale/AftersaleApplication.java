package cn.edu.xmu.oomall.aftersale;

import cn.edu.xmu.javaee.core.jpa.SelectiveUpdateJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.javaee.core",
        "cn.edu.xmu.oomall.aftersale"},
        exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableJpaRepositories(value = "cn.edu.xmu.javaee.core.jpa", repositoryBaseClass = SelectiveUpdateJpaRepositoryImpl.class, basePackages = "cn.edu.xmu.oomall.aftersale.mapper")
//@EnableMongoRepositories(basePackages = "cn.edu.xmu.oomall.aftersale.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class AftersaleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AftersaleApplication.class, args);
    }

}
