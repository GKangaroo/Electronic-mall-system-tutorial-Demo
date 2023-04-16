package cn.edu.xmu.oomall.customer.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Serializable;

@NoArgsConstructor
@Data
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItem extends OOMallObject implements Serializable {


    private Long productId;
    private Long orderId;
    private String name;
    private Integer quantity;
    private Integer price;
    private Integer discountPrice;

}
