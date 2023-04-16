package cn.edu.xmu.oomall.customer.dao.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class Shop {

    private Long id;
    private String name;
    private Integer type;
}
