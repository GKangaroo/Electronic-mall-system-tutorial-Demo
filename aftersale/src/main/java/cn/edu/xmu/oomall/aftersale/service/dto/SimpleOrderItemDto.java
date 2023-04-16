package cn.edu.xmu.oomall.aftersale.service.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleOrderItemDto
{
    private Integer productId;
    private Integer orderId;
    private String name;
    private Integer quantity;
    private Integer price;
    private Integer discountPrice;
}
