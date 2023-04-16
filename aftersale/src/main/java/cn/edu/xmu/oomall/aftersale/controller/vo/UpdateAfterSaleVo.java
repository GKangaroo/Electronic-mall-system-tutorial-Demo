package cn.edu.xmu.oomall.aftersale.controller.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateAfterSaleVo {

    @Min(value = 0,message = "数量不能小于0")
    private Integer quantity;

    private String reason;

    @NotNull(message = "联系人不能为空")
    private ConsigneeVo consignee;

}
