package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.oomall.customer.dao.CouponDao;
import cn.edu.xmu.oomall.customer.dao.bo.Coupon;
import cn.edu.xmu.oomall.customer.mapper.CouponPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.CouponPo;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

@Repository
@Component
@RocketMQMessageListener(topic = "createCoupon", consumerGroup = "test-group", selectorExpression = "*", messageModel = MessageModel.CLUSTERING)
public class CouponCreateService implements RocketMQListener<Coupon> {

    private CouponDao couponDao;

    @Autowired
    public CouponCreateService(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @Override
    public void onMessage(Coupon bo) {
        couponDao.createCoupon(bo);
    }
}
