package cn.edu.xmu.oomall.aftersale.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.aftersale.dao.bo.Shop;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("shop-service")
@Repository
public interface ShopDao {

    @GetMapping("/shops/{id}")
    InternalReturnObject<Shop> getShopById(@PathVariable Long id);


}
