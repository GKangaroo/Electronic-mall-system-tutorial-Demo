package cn.edu.xmu.oomall.aftersale.controller.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class NewArbitrationVo {

    @NotNull(message = "类型不能为空!")
    private Byte type;

    private Integer quantity;
    private String reason;
    private Long regionId;
    private String detail;
    private String consignee;
    private String mobile;




}
