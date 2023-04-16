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
public class SuccessShareDto {
    private Long id;
    private SimpleOnsaleDto onsale;
    private SimpleShareDto share;
    private SimpleCustomerDto customer;
    private SimpleOrderItemDto orderItem;
    //返点
    private Integer rebate;
    private LocalDateTime gmtCreate;
    private SimpleAdminUserDto creator;

}
