package cn.edu.xmu.oomall.customer.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewCouponVo {

    @NotBlank(message="优惠卷序号不能为空")
    private String couponSn;

    @NotNull(message = "优惠卷名称不能为空!")
    private Long name;

    @Future(message = "优惠卷开始时间之后才有效")
    private LocalDateTime beginTime;

    @Past(message = "优惠卷结束时间之前才有效")
    private LocalDateTime endTime;

}
