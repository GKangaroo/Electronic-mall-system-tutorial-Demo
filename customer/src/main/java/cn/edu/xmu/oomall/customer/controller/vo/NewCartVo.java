package cn.edu.xmu.oomall.customer.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewCartVo {

    @NotNull(message = "商品ID不能为空!")
    private Long productId;

    @Min(value = 1,message = "数量不能低于1")
    private Integer quantity;

}
