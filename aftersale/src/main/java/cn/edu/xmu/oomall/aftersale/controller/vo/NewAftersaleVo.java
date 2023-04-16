package cn.edu.xmu.oomall.aftersale.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewAftersaleVo {
    @NotBlank(message = "类型不能为空")
    Byte type;

    @NotBlank(message = "数量不能为空")
    Integer quantity;

    @NotBlank(message = "必须填写原因")
    String reason;

    @NotBlank(message = "必须填写联系人信息")
    ConsigneeVo consignee;
}
