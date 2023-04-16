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
public class SimpleCouponDto {

    private Long id;
    private SimpleCouponActivityDto activity;
    private String name;
    //优惠券编号
    private String couponSn;
}
