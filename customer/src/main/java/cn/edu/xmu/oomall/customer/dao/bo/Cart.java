package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.customer.dao.openFeign.CouponActivityDao;
import cn.edu.xmu.oomall.customer.dao.openFeign.ProductDao;
import cn.edu.xmu.oomall.customer.mapper.po.CartPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Builder
public class Cart extends OOMallObject implements Serializable {

    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(Cart.class);


    @Setter
    @Getter
    private Long customerId;

    @Setter
    @Getter
    private Integer quantity;

    @Setter
    @Getter
    private Long price;

    @Setter
    @Getter
    private Long productId;

    @JsonIgnore
    private Product product;

    @Setter
    @JsonIgnore
    private ProductDao productDao;

    public Product getProduct(){
        if (null == this.product && null != this.productDao){
            InternalReturnObject<Product> ret = this.productDao.getSimpleProductById(this.productId);
            logger.debug("getProduct: ret ={}", ret);
            if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())){
                this.product = ret.getData();
            }
        }
        return this.product;
    }

    @JsonIgnore
    private List<CouponActivity> couponActivityList;

    @Setter
    @JsonIgnore
    private CouponActivityDao couponActivityDao;

    public List<CouponActivity> getCouponActivityList(){
        if (null == this.couponActivityList && null != this.couponActivityDao){
            InternalReturnObject<List<CouponActivity>> ret = this.couponActivityDao.getCouponActivityByProductId(this.productId);
            logger.debug("getList<CouponActivity>: ret ={}", ret);
            if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())){
                this.couponActivityList = ret.getData();
            }
        }
        return this.couponActivityList;
    }


}
