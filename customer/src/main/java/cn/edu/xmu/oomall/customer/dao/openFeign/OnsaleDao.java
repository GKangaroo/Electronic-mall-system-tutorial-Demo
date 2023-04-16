package cn.edu.xmu.oomall.customer.dao.openFeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.oomall.customer.dao.bo.CouponActivity;
import cn.edu.xmu.oomall.customer.dao.bo.Onsale;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("tmp1-service")
@Repository
public interface OnsaleDao {

    // TODO: 2022/12/21 目前goods模块没有这个接口，需要自己写
    //根据productId返回onsale
    @GetMapping("/onsales")
    InternalReturnObject<Onsale> retrieveOnsales(@RequestParam Long productId, @RequestParam Long shopId);

    InternalReturnObject<Onsale> getOnsaleByProductId(@RequestParam Long productId);

    @GetMapping("/onsales/{id}")
    InternalReturnObject<Onsale> getOnsaleById(@PathVariable Long id);




}
