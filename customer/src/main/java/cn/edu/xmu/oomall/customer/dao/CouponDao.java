package cn.edu.xmu.oomall.customer.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.customer.dao.bo.Coupon;
import cn.edu.xmu.oomall.customer.dao.bo.CouponActivity;
import cn.edu.xmu.oomall.customer.dao.openFeign.CouponActivityDao;
import cn.edu.xmu.oomall.customer.mapper.CouponPoMapper;
import cn.edu.xmu.oomall.customer.mapper.po.CouponPo;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

@Repository
public class CouponDao {

    private static final Logger logger = LoggerFactory.getLogger(CouponDao.class);

    public static final String KEY = "CO%d";

    //    @Value("${oomall.coupon.timeout}")
    private int timeout;

    private RedisUtil redisUtil;

    private CouponActivityDao couponActivityDao;

    private CouponPoMapper couponPoMapper;


    @Autowired
    public CouponDao(RedisUtil redisUtil, CouponPoMapper couponPoMapper) {
        this.redisUtil = redisUtil;
        this.couponPoMapper = couponPoMapper;
    }

    private Coupon getBo(CouponPo po, String redisKey) {
        Coupon ret = cloneObj(po, Coupon.class);
        if (null != redisKey) {
            redisUtil.set(redisKey, ret, timeout);
        }
        this.setBo(ret);
        return ret;
    }

    private void setBo(Coupon bo) {
        bo.setCouponActivityDao(this.couponActivityDao);
    }

    /**
     * 根据id获得对象
     *
     * @date: 2022/12/21 19:35
     */
    public Coupon findById(Long id) throws RuntimeException {
        logger.debug("findById: id ={}", id);
        if (null == id) {
            return null;
        }

        String key = String.format(KEY, id);
        if (redisUtil.hasKey(key)) {
            Coupon bo = (Coupon) redisUtil.get(key);
            setBo(bo);
            return bo;
        }

        Optional<CouponPo> retObj = this.couponPoMapper.findById(id);
        if (retObj.isEmpty()) {
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "用户", id));
        } else {
            CouponPo po = retObj.get();
            return this.getBo(po, key);
        }
    }


    /**
     * 查询优惠券
     *
     * @date: 2022/12/21 19:44
     */
    public PageDto<Coupon> retrieveCoupons(Integer status, Long actId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<CouponPo> ret = null;
        if (actId == null && status != null) {
            ret = couponPoMapper.findByUsed(status, pageable);
        } else if (actId != null && status == null) {
            ret = couponPoMapper.findByActivityId(actId, pageable);
        } else if (actId == null) {
            ret = couponPoMapper.findAll(pageable);
        } else {
            ret = couponPoMapper.findByActivityIdAndUsed(actId, status, pageable);
        }
        if (ret == null) {
            return new PageDto<>(new ArrayList<>(), page, pageSize);
        }

        List<Coupon> bos = ret.stream().map(po -> cloneObj(po, Coupon.class))
                .collect(Collectors.toList());
        return new PageDto<>(bos, page, pageSize);
    }

    public Long countCouponsByActId(Long actId){
        return couponPoMapper.countByActivityIdEquals(actId);
    }

    public void createCoupon(Coupon bo) {
        CouponPo po = cloneObj(bo, CouponPo.class);
        couponPoMapper.save(po);
    }

    public ReturnObject receiveCoupons(Long actId, Long usrId, Integer quantity) {
        List<Coupon> rets = new ArrayList<>();
        Coupon bo = new Coupon();
        bo.setActivityId(actId);
        bo.setCustomerId(usrId);
        bo.setGmtCreate(LocalDateTime.now());
        bo.setBeginTime(LocalDateTime.now());
        for (int i = 0; i < quantity; i++) {
            bo.setCouponSn(String.format("act%duser%dn%d", actId, usrId, i));
            CouponPo po = cloneObj(bo, CouponPo.class);
            CouponPo ret = couponPoMapper.save(po);
            rets.add(cloneObj(ret, Coupon.class));
        }
        return new ReturnObject(ReturnNo.OK, rets);
    }

}
