package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CouponActivity extends OOMallObject implements Serializable {


    private Long id;
    private String name;
    private Shop shop;
    private Integer quantity;
    private Integer quantityType;
    private Integer validTerm;
    private LocalDateTime couponTime;
    private String strategy;

}
