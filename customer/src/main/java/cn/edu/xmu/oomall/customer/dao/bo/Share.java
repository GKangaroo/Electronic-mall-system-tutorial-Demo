package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.oomall.customer.dao.openFeign.CouponActivityDao;
import cn.edu.xmu.oomall.customer.dao.openFeign.OnsaleDao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;


@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Share extends OOMallObject implements Serializable {


    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(Coupon.class);

    @Getter
    @Setter
    private Long customerId;

    @Getter
    @Setter
    private Long activityId;

    @Getter
    @Setter
    private Long onsaleId;

    @Getter
    @Setter
    private Integer quantity;

    @Setter
    @JsonIgnore
    private OnsaleDao onsaleDao;

    @JsonIgnore
    private Onsale onsale;

    public Onsale getOnsale(){
        if (null == this.onsale && null != this.onsaleDao){
            InternalReturnObject<Onsale> ret = this.onsaleDao.getOnsaleById(this.onsaleId);
            logger.debug("getOnsale: ret ={}", ret);
            if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())){
                this.onsale = ret.getData();
            }
        }
        return this.onsale;
    }

    @Setter
    @JsonIgnore
    private CustomerDao customerDao;

    @JsonIgnore
    private Customer customer;

    public Customer getCustomer(){
        if (null == this.customer && null != this.customerDao){
            Customer customer = this.customerDao.findById(this.customerId);
            logger.debug("findById: ret ={}", customer);
            this.customer = customer;
        }
        return this.customer;
    }




}
