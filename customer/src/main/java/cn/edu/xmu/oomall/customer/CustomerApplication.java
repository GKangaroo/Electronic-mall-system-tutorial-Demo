//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.oomall.customer;

import cn.edu.xmu.javaee.core.jpa.SelectiveUpdateJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.javaee.core",
        "cn.edu.xmu.oomall.customer"})
@EnableJpaRepositories(value = "cn.edu.xmu.javaee.core.jpa", repositoryBaseClass = SelectiveUpdateJpaRepositoryImpl.class, basePackages = "cn.edu.xmu.oomall.customer.mapper")
@EnableMongoRepositories(basePackages = "cn.edu.xmu.oomall.customer.mapper")
@EnableFeignClients
@EnableDiscoveryClient
public class CustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

}
