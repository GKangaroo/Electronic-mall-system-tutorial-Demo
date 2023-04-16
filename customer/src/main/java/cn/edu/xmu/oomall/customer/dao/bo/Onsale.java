package cn.edu.xmu.oomall.customer.dao.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;


/**
 * @author: 兰文强
 * @date: 2022/12/17 21:28
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Onsale {
    @ToString.Exclude
    @JsonIgnore
    private final static Logger logger = LoggerFactory.getLogger(Onsale.class);

    private Long id;
    private Product product;
    private Long price;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Integer quantity;
    private Integer maxQuantity;
    private Integer type;

}
