package cn.edu.xmu.oomall.order.service;

import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.oomall.order.dao.OrderDao;
import cn.edu.xmu.oomall.order.dao.OrderitemDao;
import cn.edu.xmu.oomall.order.dao.bo.Order;
import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderitemService {

    OrderDao orderDao;

    OrderitemDao orderitemDao;

    @Autowired
    OrderitemService(OrderDao orderDao, OrderitemDao orderitemDao) {
        this.orderDao = orderDao;
        this.orderitemDao = orderitemDao;
    }

    public PageDto<OrderItem> getAftersalesOrderitemsByCustomerId(Long id,
                                                                  Integer page,
                                                                  Integer pageSize) {
        List<Order> orders = orderDao.retriveByCustomerId(id, page, pageSize);
        List<OrderItem> ret = new ArrayList<>();
        orders.stream().map(order -> ret.addAll(orderitemDao.retriveByOrderId(order.getId(), page, pageSize)));
        return new PageDto<>(ret, page, pageSize);
    }
}
