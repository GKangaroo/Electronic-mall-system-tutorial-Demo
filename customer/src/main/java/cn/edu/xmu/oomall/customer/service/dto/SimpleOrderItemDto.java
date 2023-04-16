package cn.edu.xmu.oomall.customer.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor@NoArgsConstructor
@Builder
public class SimpleOrderItemDto {

    private Long productId;
    private Long orderId;
    private String name;
    private Integer quantity;
    private Long price;
    private Long discountPrice;

}
