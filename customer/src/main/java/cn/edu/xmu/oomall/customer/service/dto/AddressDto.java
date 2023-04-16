package cn.edu.xmu.oomall.customer.service.dto;

import cn.edu.xmu.oomall.customer.dao.bo.Region;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor

@Builder
public class AddressDto {

    private Long id;
    private Region region;
    private String address;
    private String consignee;
    private String mobile;
    private Boolean beDefault;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;


}
