package cn.edu.xmu.oomall.customer.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewAddressVo {

    @NotNull(message = "地址ID不能为空!")
    private Long regionId;

    @NotBlank(message="详细地址不能为空")
    private String address;

    @NotBlank(message="联系人不能为空")
    private String consignee;

    @NotBlank(message="联系电话不能为空")
    private String mobile;

}
