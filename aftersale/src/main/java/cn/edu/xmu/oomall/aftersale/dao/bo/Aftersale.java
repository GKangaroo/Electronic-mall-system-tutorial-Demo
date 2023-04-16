package cn.edu.xmu.oomall.aftersale.dao.bo;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.aftersale.dao.AftersaleDao;
import cn.edu.xmu.oomall.aftersale.dao.HistoryDao;
import cn.edu.xmu.oomall.aftersale.dao.openfeign.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 兰文强
 * @date: 2022/12/22 16:53
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public abstract class Aftersale extends OOMallObject implements Serializable {


    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(Aftersale.class);

    /**
     * 申请
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte NEW = 0;
    /**
     * 处理中
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte PROCESS = 1;
    // TODO: 2022/12/22 这个状态，原本是7，改成了2
    /**
     * 分派服务商
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte DISPATCHING = 2;
    /**
     * 待退款
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte REFUNDING = 3;
    /**
     * 发出换货订单
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte REPLACING = 4;
    /**
     * 取消
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte CANCEL = 5;
    /**
     * 结束
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte END = 6;

    /**
     * 状态和名称的对应
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Map<Byte, String> STATUSNAMES = new HashMap(){
        {
            put(NEW, "申请");
            put(PROCESS, "处理中");
            put(DISPATCHING, "分派服务商");
            put(REFUNDING, "待退款");
            put(REPLACING, "发出换货订单");
            put(END, "结束");
            put(CANCEL, "取消");
        }
    };

    /**
     * 获得当前状态名称
     */
    @JsonIgnore
    public String getStatusName(){
        return STATUSNAMES.get(this.status);
    }



    /**
     * 换货
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte REPLACE = 0;

    /**
     * 换货
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte RETURN = 1;

    /**
     * 维修
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Byte REPAIR = 2;


    /**
     * 类型和名称的对应
     */
    @JsonIgnore
    @ToString.Exclude
    public static final Map<Byte, String> TYPENAMES = new HashMap(){
        {
            put(REPLACE, "换货");
            put(RETURN, "退货");
            put(REPAIR, "维修");
        }
    };

    /**
     * 获得当前状态名称
     */
    @JsonIgnore
    public String getTypeName(){
        return STATUSNAMES.get(this.type);
    }



    @Getter
    @Setter
    private Long orderItemId;
    @Getter
    @Setter
    private Long customerId;
    @Getter
    @Setter
    private Long shopId;
    @Getter
    @Setter
    private String aftersaleSn;
    @Getter
    @Setter
    private Byte type;
    @Getter
    @Setter
    private String reason;
    @Getter
    @Setter
    private String conclusion;
    @Getter
    private Integer quantity;
    //检查申请的数量小于订单明细中有效商品的数量（未退货换货）
    public void setQuantity(Integer quantity){
        Integer or = this.getOrderItem().getQuantity();
        if(quantity>or){
            throw new BusinessException(ReturnNo.FIELD_NOTVALID,String.format(ReturnNo.FIELD_NOTVALID.getMessage(),"数量"));
        }
    }
    @Getter
    @Setter
    private Long regionId;
    @Getter
    @Setter
    private String address;
    @Getter
    @Setter
    private String consignee;
    @Getter
    @Setter
    private String mobile;
    @Getter
    @Setter
    private Byte status;
    @Getter
    @Setter
    private Byte inArbitrated;
    @Getter
    @Setter
    private Long serviceId;
    @Getter
    @Setter
    private String serialNo;
    @Getter
    @Setter
    private String name;

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
    private ShopDao shopDao;

    @JsonIgnore
    private Shop shop;

    public Shop getShop(){
        if (null == this.shop && null != this.shopDao){
            InternalReturnObject<Shop> ret = this.shopDao.getShopById(this.shopId);
            logger.debug("getShopById: ret ={}", ret);
            if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())){
                this.shop = ret.getData();
            }
        }
        return this.shop;
    }


    @Setter
    @JsonIgnore
    private ProductDao productDao;

    @JsonIgnore
    private Product product;

    public Product getProduct(){
        if (null == this.product && null != this.productDao){
            InternalReturnObject<Product> ret = this.productDao.getProductById(this.getOrderItem().getProductId());
            logger.debug("getProductById: ret ={}", ret);
            if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())){
                this.product = ret.getData();
            }
        }
        return this.product;
    }


    @Setter
    @JsonIgnore
    private CustomerDao customerDao;

    @JsonIgnore
    private Customer customer;

    public Customer getCustomer(){
        if (null == this.customer && null != this.customerDao){
            InternalReturnObject<Customer> ret = this.customerDao.getCustomerById(this.getCustomerId());
            logger.debug("getCustomerById: ret ={}", ret);
            if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())){
                this.customer = ret.getData();
            }
        }
        return this.customer;
    }


    @Setter
    @JsonIgnore
    private ServiceDao serviceDao;

    @JsonIgnore
    private Service service;

    public Service getService(){
        if (null == this.service && null != this.serviceDao){
            InternalReturnObject<Service> ret = this.serviceDao.getServiceById(this.serviceId);
            logger.debug("getServiceById: ret ={}", ret);
            if (ReturnNo.OK == ReturnNo.getByCode(ret.getErrno())){
                this.service = ret.getData();
            }
        }
        return this.service;
    }

    @Setter
    @JsonIgnore
    private AftersaleDao aftersaleDao;


    /*修改售后单信息*/
    public void update(UserDto userDto){
        //不为申请态
        if(!Aftersale.NEW.equals(this.getStatus())) {
            throw new BusinessException(ReturnNo.STATENOTALLOW,String.format(ReturnNo.STATENOTALLOW.getMessage(),
                    "售后单",id,this.getStatusName()));
        }
        aftersaleDao.saveById(this,userDto);
    }

    /*顾客取消售后单*/
    public void cancel(UserDto userDto){
        //不处于申请态和处理态
        if(!this.getStatus().equals(Aftersale.NEW)&&!this.getStatus().equals(Aftersale.PROCESS)){
            throw new BusinessException(ReturnNo.STATENOTALLOW,
                    String.format(ReturnNo.STATENOTALLOW.getMessage(),"售后单",id,this.getStatusName()));
        }
        aftersaleDao.cancelById(id, userDto);
    }

    /*店家同意/拒绝售后单*/
    public abstract void confirmAftersale(boolean confirm, UserDto userDto);


    /*根据查询到的状态，更新售后单的状态*/
    public abstract void query(Byte status);


    /*商家验收买家寄回的货物*/
    public abstract void confirmReceive(boolean confirm);



}