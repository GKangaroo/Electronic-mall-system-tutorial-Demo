package cn.edu.xmu.oomall.service.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author：guiqingxin
 * @date：2022/12/22 23:47
 */
@Data
@NoArgsConstructor
public class ServiceConsigneeVo {
    /**
     * 服务商联系人姓名和电话
     */
    private String name;
    private String mobile;

    @NotNull(message = "地区必填")
    private Long regionId;

    @NotBlank(message = "服务商联系人详细地址不能为空")
    private String address;
}
