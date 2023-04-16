package cn.edu.xmu.oomall.service.config;

import cn.edu.xmu.javaee.core.util.JwtHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author：guiqingxin
 * @date：2022/12/20 13:17
 */
@Component
public class StartupRunner implements ApplicationRunner {
    private final Logger logger = LoggerFactory.getLogger(StartupRunner.class);


    @Override
    public void run(ApplicationArguments args) throws Exception {
        JwtHelper jwtHelper = new JwtHelper();
        String adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
        logger.info("test token = {}", adminToken);
    }
}
