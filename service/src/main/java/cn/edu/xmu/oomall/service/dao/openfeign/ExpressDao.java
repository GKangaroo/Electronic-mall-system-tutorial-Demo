package cn.edu.xmu.oomall.service.dao.openfeign;

import cn.edu.xmu.oomall.service.dao.openfeign.Bo.Express;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author：guiqingxin
 * @date：2023/1/5 18:07
 */
@FeignClient("freight-service")
public interface ExpressDao {
    @GetMapping("/internal/shops/{shopId}/packages")
    Express createExpress();
}
