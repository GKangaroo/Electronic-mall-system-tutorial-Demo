package cn.edu.xmu.oomall.aftersale.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.aftersale.dao.bo.OrderItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("prodorder-service")
@Repository
public interface OrderItemDao {


    //根据orderItemId返回orderItemId
    @GetMapping("/orderItem/{id}")
    InternalReturnObject<OrderItem> getOrderItemById(@PathVariable Long id);

    @GetMapping("/customer/{id}/aftersales/orderitems")
    InternalReturnObject<List<OrderItem>> getAftersalesOrderitemsByCustomerId(@PathVariable Long id,
                                                                              @RequestParam("page") Integer page,
                                                                              @RequestParam("pageSize") Integer pageSize);

}
