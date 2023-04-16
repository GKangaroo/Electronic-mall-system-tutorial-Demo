package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.customer.dao.openFeign.CouponActivityDao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author: 兰文强
 * @date: 2022/12/17 21:23
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Coupon extends OOMallObject implements Serializable {

    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(Coupon.class);

    @Setter
    @Getter
    private String couponSn;

    @Setter
    @Getter
    private Long name;

    @Setter
    @Getter
    private Long customerId;

    @Setter
    @Getter
    private Long activityId;

    @Setter
    @Getter
    private LocalDateTime beginTime;

    @Setter
    @Getter
    private LocalDateTime endTime;

    @Setter
    @Getter
    private Integer used;

    @Setter
    @JsonIgnore
    private CouponActivityDao couponActivityDao;

    @JsonIgnore
    private CouponActivity couponActivity;

    public CouponActivity getCouponActivity(){
        if (null == this.couponActivity && null != this.couponActivityDao){
            InternalReturnObject<CouponActivity> ret = this.couponActivityDao.getCouponActivityById(this.activityId);
            logger.debug("getCouponActivity: ret ={}", ret);
            if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())){
                this.couponActivity = ret.getData();
            }
        }
        return this.couponActivity;
    }




}
