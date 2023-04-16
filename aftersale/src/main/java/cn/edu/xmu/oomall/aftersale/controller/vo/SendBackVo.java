package cn.edu.xmu.oomall.aftersale.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendBackVo {

    @NotBlank(message = "寄回运单号不能为空")
    private String logSn;



}
