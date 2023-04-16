package cn.edu.xmu.oomall.customer.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor@NoArgsConstructor
@Builder
public class SimpleCouponActivityDto {

    private Long id;
    private String name;
    private Integer quantity;
    //开始领取优惠券的时间
    private LocalDateTime couponTime;

}
