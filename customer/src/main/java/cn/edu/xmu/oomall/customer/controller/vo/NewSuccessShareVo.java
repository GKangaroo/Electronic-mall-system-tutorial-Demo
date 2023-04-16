package cn.edu.xmu.oomall.customer.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class NewSuccessShareVo {

    @Min(value = 1,message = "返点数不能低于1")
    private Integer rebate;

}
