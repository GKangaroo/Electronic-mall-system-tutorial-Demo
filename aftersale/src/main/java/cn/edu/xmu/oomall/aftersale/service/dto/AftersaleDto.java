package cn.edu.xmu.oomall.aftersale.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AftersaleDto
{
    Long id;

    SimpleOrderItemDto orderItem;

    SimpleAdminUserDto customer;

    SimpleShopDto shop;

    String aftersaleSn;

    Byte type;

    String reason;

    String conclusion;

    Long quantity;

    ConsigneeDto consignee;

    Byte status;

    Byte inArbitrated;

    ProductNameDto product;

    SimpleAdminUserDto creator;

    LocalDateTime gmtCreate;

    LocalDateTime gmtModified;

    SimpleAdminUserDto modifier;

}
