package cn.edu.xmu.oomall.customer.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.customer.dao.CouponDao;
import cn.edu.xmu.oomall.customer.dao.bo.Coupon;
import cn.edu.xmu.oomall.customer.dao.bo.CouponActivity;
import cn.edu.xmu.oomall.customer.dao.openFeign.CouponActivityDao;
import cn.edu.xmu.oomall.customer.service.dto.SimpleCouponActivityDto;
import cn.edu.xmu.oomall.customer.service.dto.SimpleCouponDto;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

@Service
public class CouponService {

    private static final Logger logger = LoggerFactory.getLogger(CouponService.class);

    public static final String KEY_Getted = "U%dA%d";

    public static final String KEY_Act = "CAO%d";

    public static final String KEY_Act_Quantity = "CA%d";

    public static final String KEY_Get = "CAGET%d";

    private CouponDao couponDao;

    private CouponActivityDao couponActivityDao;

    private RedisUtil redisUtil;

    private RocketMQTemplate rocketMQTemplate;


    @Autowired
    public CouponService(CouponDao couponDao, RedisUtil redisUtil, CouponActivityDao couponActivityDao,RocketMQTemplate rocketMQTemplate) {
        this.couponDao = couponDao;
        this.redisUtil = redisUtil;
        this.couponActivityDao = couponActivityDao;
        this.rocketMQTemplate = rocketMQTemplate;
    }

    /**
     * 顾客查看优惠卷列表
     */
    public PageDto<SimpleCouponDto> retrieveCoupons(Integer status, Long actId, Integer page, Integer pageSize, UserDto userDto) {
        PageDto<Coupon> coupons = couponDao.retrieveCoupons(status, actId, page, pageSize);
        List<SimpleCouponDto> ret = coupons.getList().stream().map(bo -> {
            SimpleCouponDto dto = cloneObj(bo, SimpleCouponDto.class);
            dto.setActivity(cloneObj(bo.getCouponActivity(), SimpleCouponActivityDto.class));
            return dto;
        }).collect(Collectors.toList());
        return new PageDto<>(ret, page, pageSize);
    }

    /**
     * 买家领取活动优惠券，上线状态才能领取
     */
    public ReturnObject getCouponById(Long id, UserDto userDto) {
        Long userId = userDto.getId();
        if (redisUtil.hasKey(String.format(KEY_Getted, userId, id))) {
            throw new BusinessException(ReturnNo.COUPON_EXIST, ReturnNo.COUPON_EXIST.getMessage());
        }
        String key_act_quantity = String.format(KEY_Act_Quantity, id);
        if (redisUtil.hasKey(key_act_quantity)) {
            if (redisUtil.decr(key_act_quantity, 1) < 0) {
                return new ReturnObject(ReturnNo.COUPON_FINISH);
            } else {
                redisUtil.set(String.format(KEY_Getted, userId, id), 1, -1);
                String sn = String.format("act%duser%d", id, userId);
                Coupon bo = Coupon.builder().activityId(id).customerId(userId).beginTime(LocalDateTime.now()).couponSn(sn).build();
                bo.setGmtCreate(LocalDateTime.now());
                rocketMQTemplate.syncSend("createCoupon", MessageBuilder.withPayload(bo).build(), 2000, 3);
                return new ReturnObject(ReturnNo.OK, sn);
            }
        } else {
            String key_activity = String.format(KEY_Act, id);
            CouponActivity couponActivity = null;
            if (redisUtil.hasKey(key_activity)) {
                couponActivity = (CouponActivity) redisUtil.get(key_activity);
            } else {
                String key_getting = String.format(KEY_Get, id);
                int c = 10;
                while (redisUtil.hasKey(key_getting) && c > 0) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }
                    c--;
                }
                if (redisUtil.hasKey(key_activity)) {
                    couponActivity = (CouponActivity) redisUtil.get(key_activity);
                } else {
                    redisUtil.set(key_getting, 1, 5);
                    couponActivity = couponActivityDao.getCouponActivityById(id).getData();
                    redisUtil.set(key_activity, couponActivity, 3600);
                    redisUtil.del(key_getting);
                }
            }
            if (couponActivity.getCouponTime().compareTo(LocalDateTime.now()) <= 0) {
                if (couponActivity.getQuantityType() == 1) {
                    Long count = couponDao.countCouponsByActId(id);
                    if (!redisUtil.hasKey(key_act_quantity)) {
                        redisUtil.set(key_act_quantity, couponActivity.getQuantity() - count, -1);
                    }
                    if (redisUtil.decr(key_act_quantity, 1) < 0) {
                        return new ReturnObject(ReturnNo.COUPON_FINISH);
                    } else {
                        redisUtil.set(String.format(KEY_Getted, userId, id), 1, -1);
                        String sn = String.format("act%duser%d", id, userId);
                        Coupon bo = Coupon.builder().activityId(id).customerId(userId).beginTime(LocalDateTime.now()).couponSn(sn).build();
                        bo.setGmtCreate(LocalDateTime.now());
                        rocketMQTemplate.syncSend("createCoupon", MessageBuilder.withPayload(bo).build(), 2000, 3);
                        return new ReturnObject(ReturnNo.OK, sn);
                    }
                } else {
                    return couponDao.receiveCoupons(id, userId, couponActivity.getQuantity());
                }
            } else {
                return new ReturnObject(ReturnNo.COUPON_NOTBEGIN);
            }
        }
    }


}
