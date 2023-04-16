package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.customer.dao.CustomerDao;
import cn.edu.xmu.oomall.customer.dao.ShareDao;
import cn.edu.xmu.oomall.customer.dao.openFeign.OnsaleDao;
import cn.edu.xmu.oomall.customer.dao.openFeign.OrderItemDao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.aspectj.weaver.ast.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * @author: 兰文强
 * @date: 2022/12/17 21:27
 */
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessShare extends OOMallObject implements Serializable {
    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(SuccessShare.class);


    @Setter
    @Getter
    private Long shareId;

    @Setter
    @Getter
    private Long orderItemId;

    @Setter
    @Getter
    private Long customerId;

    @Setter
    @Getter
    private Long rebate;

    @Builder
    public SuccessShare(Long id, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified, Long shareId, Long orderItemId, Long customerId, Long rebate) {
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.shareId = shareId;
        this.orderItemId = orderItemId;
        this.customerId = customerId;
        this.rebate = rebate;
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

    @Setter
    @JsonIgnore
    private ShareDao shareDao;

    @JsonIgnore
    private Share share;

    public Share getShare(){
        if (null == this.share && null != this.shareDao){
            Share share = this.shareDao.findById(this.shareId);
            logger.debug("findById: ret ={}", share);
            this.share = share;
        }
        return this.share;
    }


    @Setter
    @JsonIgnore
    private OrderItemDao orderItemDao;

    @JsonIgnore
    private OrderItem orderItem;

    public OrderItem getOrderItem(){
        if (null == this.orderItem && null != this.orderItemDao){
            InternalReturnObject<OrderItem> ret = this.orderItemDao.getOrderItemById(this.orderItemId);
            logger.debug("getOrderItem: ret ={}", ret);
            if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())){
                this.orderItem = ret.getData();
            }
        }
        return this.orderItem;
    }


    @Setter
    @JsonIgnore
    private OnsaleDao onsaleDao;

    @JsonIgnore
    private Onsale onsale;

    public Onsale getOnsale(){
        if (null == this.onsale && null != this.onsaleDao){
            InternalReturnObject<Onsale> ret = this.onsaleDao.getOnsaleByProductId(this.getOrderItem().getProductId());
            logger.debug("getOrderItem: ret ={}", ret);
            if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())){
                this.onsale = ret.getData();
            }
        }
        return this.onsale;
    }






}
