package cn.edu.xmu.oomall.customer.dao.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: 兰文强
 * @date: 2022/12/17 21:20
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Long id;

    private String name;

    private Long price;

    private Integer quantity;

    private Byte status;

    private List<Activity> actList;

}
