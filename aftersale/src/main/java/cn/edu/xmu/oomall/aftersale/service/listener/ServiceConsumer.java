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

//监听服务单状态的变化
@Service
@RocketMQMessageListener(topic = "Service", consumerGroup = "aftersale-service", consumeThreadMax = 1)
public class ServiceConsumer implements RocketMQListener<Message> {

    private static final Logger logger = LoggerFactory.getLogger(ServiceConsumer.class);

    private AftersaleService aftersaleService;

    @Autowired
    public ServiceConsumer(AftersaleService aftersaleService) {
        this.aftersaleService = aftersaleService;
    }

    @Override
    public void onMessage(Message message) {

        try {
            String content = new String(message.getBody(), "UTF-8");
            cn.edu.xmu.oomall.aftersale.dao.bo.Service service = JacksonUtil.toObj(content, cn.edu.xmu.oomall.aftersale.dao.bo.Service.class);
            if (null == service || null == service.getStatus()){
                logger.error("ServiceConsumer: wrong format.... content = {}",content);
            }else{
                aftersaleService.serviceQuery(service.getId(),service.getStatus());
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("ServiceConsumer: wrong encoding.... msg = {}", message);
        }

    }
}
