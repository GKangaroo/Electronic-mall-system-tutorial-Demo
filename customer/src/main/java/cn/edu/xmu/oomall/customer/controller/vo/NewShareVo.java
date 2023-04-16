package cn.edu.xmu.oomall.customer.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewShareVo {

    @Min(value = 1,message = "成功返点商品数不能低于1")
    private Integer quantity;

}
