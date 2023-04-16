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
public class ShareDto {

    private Long id;
    private SimpleCustomerDto customer;
    private SimpleOnsaleDto onsale;
    //分享次数累计
    private Integer quantity;
    private LocalDateTime gmtCreate;
    private SimpleAdminUserDto creator;


}
