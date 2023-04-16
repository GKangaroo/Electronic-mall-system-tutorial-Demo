package cn.edu.xmu.oomall.aftersale.service.listener;

import cn.edu.xmu.javaee.core.util.JacksonUtil;
import cn.edu.xmu.oomall.aftersale.dao.bo.Order;
import cn.edu.xmu.oomall.aftersale.service.AftersaleService;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

//订单完成，售后单结束
@Service
@RocketMQMessageListener(topic = "End-Order", consumerGroup = "aftersale-end-order", consumeThreadMax = 1)
public class OrderConsumer implements RocketMQListener<Message> {

    private static final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    private AftersaleService aftersaleService;

    @Autowired
    public OrderConsumer(AftersaleService aftersaleService) {
        this.aftersaleService = aftersaleService;
    }

    @Override
    public void onMessage(Message message) {

        try {
            String content = new String(message.getBody(), "UTF-8");
            Order order = JacksonUtil.toObj(content, Order.class);
            if (null == order || null == order.getState()){
                logger.error("OrderConsumer: wrong format.... content = {}",content);
            }else{
                aftersaleService.orderQuery(order.getId(),order.getState());
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("OrderConsumer: wrong encoding.... msg = {}",message);
        }

    }
}
