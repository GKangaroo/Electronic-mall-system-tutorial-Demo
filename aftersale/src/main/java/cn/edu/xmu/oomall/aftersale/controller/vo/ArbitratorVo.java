package cn.edu.xmu.oomall.aftersale.controller.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ArbitratorVo {

    @NotNull(message = "User Id 不能为空")
    private Long arbitrationId;

    private String arbitrationName;

}
