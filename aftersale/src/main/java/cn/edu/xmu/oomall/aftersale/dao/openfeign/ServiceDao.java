package cn.edu.xmu.oomall.aftersale.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.aftersale.dao.bo.Service;
import cn.edu.xmu.oomall.aftersale.dao.bo.Shop;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-service")
@Repository
public interface ServiceDao {

    @GetMapping("/service/{id}")
    InternalReturnObject<Service> getServiceById(@PathVariable Long id);


}
