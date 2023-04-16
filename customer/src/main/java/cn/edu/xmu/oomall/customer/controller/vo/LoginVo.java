package cn.edu.xmu.oomall.customer.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginVo {
    @NotBlank(message = "用户名不能为空!")
    private String userName;
    @NotBlank(message = "密码不能为空!")
    private String password;

}
