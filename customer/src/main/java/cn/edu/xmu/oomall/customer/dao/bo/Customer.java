package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import cn.edu.xmu.oomall.customer.dao.AddressDao;
import cn.edu.xmu.oomall.customer.mapper.po.CustomerPo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyFrom(CustomerPo.class)
public class Customer extends OOMallObject implements Serializable {

    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(Customer.class);

    @Getter
    @Setter
    private String userName;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private Long point;

    @Getter
    @Setter
    private Byte status;

    @Getter
    @Setter
    private Byte beDeleted;

    @Getter
    @Setter
    private String mobile;

    @Setter
    @JsonIgnore
    private AddressDao addressDao;

//    @JsonIgnore
//    private Address address;
//

//    public Address getAddress(){
//        if (null == this.address && null != this.addressDao){
//            this.address = this.addressDao.findProductById(this.productId);
//        }
//        return this.product;
//    }


}
