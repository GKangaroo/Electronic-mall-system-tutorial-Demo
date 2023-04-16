package cn.edu.xmu.oomall.aftersale.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfirmVo {

    @NotNull(message = "确认信息不能为空!")
    private boolean confirm;

    private String conclusion;

    @NotNull(message = "售后类型不能为空!")
    private Byte type;

}
