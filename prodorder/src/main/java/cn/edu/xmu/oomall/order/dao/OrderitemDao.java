package cn.edu.xmu.oomall.order.dao;

import cn.edu.xmu.oomall.order.dao.bo.OrderItem;
import cn.edu.xmu.oomall.order.mapper.OrderItemPoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

@Repository
public class OrderitemDao {

    private OrderItemPoMapper orderItemPoMapper;

    @Autowired
    public OrderitemDao(OrderItemPoMapper orderItemPoMapper) {
        this.orderItemPoMapper = orderItemPoMapper;
    }

    public List<OrderItem> retriveByOrderId(Long orderId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        return orderItemPoMapper.findByOrderIdEquals(orderId, pageable)
                .stream().map(po -> cloneObj(po, OrderItem.class))
                .collect(Collectors.toList());
    }

}
