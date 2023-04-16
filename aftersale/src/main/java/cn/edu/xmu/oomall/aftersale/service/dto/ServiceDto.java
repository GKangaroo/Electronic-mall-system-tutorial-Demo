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
public class ServiceDto {

    Long id;

    int type;

    String consignee;

    SimpleRegionDto region;

    String address;

    String mobile;

    int status;

    String maintainerName;

    String maintainerMobile;

    String description;

    String result;

    SimpleShopDto maintainer;

    SimpleAdminUserDto creator;

    LocalDateTime gmtCreate;

    LocalDateTime gmtModified;

    SimpleAdminUserDto modifier;

}
