package cn.edu.xmu.oomall.customer.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewPasswordVo {

    @NotBlank(message = "验证码不能为空!")
    private String captcha;

    @NotBlank(message = "新密码不能为空!")
    private String newPassword;

}
