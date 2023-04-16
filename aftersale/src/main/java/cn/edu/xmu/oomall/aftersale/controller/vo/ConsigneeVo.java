package cn.edu.xmu.oomall.aftersale.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConsigneeVo {
    @NotBlank(message = "姓名不能为空")
    private String name;
    @NotBlank(message = "电话不能为空")
    private String mobile;
    @NotNull(message = "地址不能为空")
    private Long regionId;
    @NotBlank(message = "地址不能为空")
    private String address;
}
