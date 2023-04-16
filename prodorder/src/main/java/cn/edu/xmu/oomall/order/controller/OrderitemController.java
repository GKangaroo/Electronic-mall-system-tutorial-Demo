package cn.edu.xmu.oomall.order.controller;

import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.oomall.order.service.OrderitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class OrderitemController {

    private OrderitemService orderitemService;

    @Autowired
    OrderitemController(OrderitemService orderitemService) {
        this.orderitemService = orderitemService;
    }

    @GetMapping("/customer/{id}/aftersales/orderitems")
    public ReturnObject getAftersalesOrderitemsByCustomerId(@PathVariable Long id,
                                                            @RequestParam("page") Integer page,
                                                            @RequestParam("pageSize") Integer pageSize) {
        return new ReturnObject(orderitemService.getAftersalesOrderitemsByCustomerId(id, page, pageSize));
    }

}
