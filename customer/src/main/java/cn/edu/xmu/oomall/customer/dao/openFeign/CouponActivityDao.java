package cn.edu.xmu.oomall.customer.dao.openFeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.customer.dao.bo.CouponActivity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("goods-service")
@Repository // TODO: 2022/12/21 需要加这个注解吗？
public interface CouponActivityDao {

    @GetMapping("/shops/{shopId}/couponactivities/{id}")
    InternalReturnObject<CouponActivity> getCouponActivityById(@PathVariable Long id);


    // TODO: 2022/12/24 此API无，需要自己写
    //根据productId获取活动
    @GetMapping("/couponactivity")
    InternalReturnObject<List<CouponActivity>> getCouponActivityByProductId(@RequestParam Long productId);


}
