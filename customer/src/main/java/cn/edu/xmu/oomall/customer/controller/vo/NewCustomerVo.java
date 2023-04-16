package cn.edu.xmu.oomall.customer.controller.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewCustomerVo {

    @NotBlank(message="用户名称不能为空")
    private String userName;

    @NotBlank(message="密码不能为空")
    private String password;

    @NotBlank(message="真实姓名不能为空")
    private String name;

    @NotBlank(message="电话不能为空")
    private String mobile;

}
