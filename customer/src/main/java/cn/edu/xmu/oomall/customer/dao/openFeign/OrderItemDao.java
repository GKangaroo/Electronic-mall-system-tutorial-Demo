package cn.edu.xmu.oomall.customer.dao.openFeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.customer.dao.bo.OrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("tmp2-service")
@Repository
public interface OrderItemDao {

    // TODO: 2022/12/21 目前goods模块没有这个接口，需要自己写
    //根据orderItemId返回orderItemId
    @GetMapping("/orderItem/{id}")
    InternalReturnObject<OrderItem> getOrderItemById(@PathVariable Long id);


}
