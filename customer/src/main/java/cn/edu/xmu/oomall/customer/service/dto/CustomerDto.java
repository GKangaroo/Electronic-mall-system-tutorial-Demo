package cn.edu.xmu.oomall.customer.service.dto;

import cn.edu.xmu.javaee.core.aop.CopyFrom;
import cn.edu.xmu.oomall.customer.dao.bo.Customer;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Builder@NoArgsConstructor
@CopyFrom(Customer.class)
public class CustomerDto {

    private Long id;
    //用户名
    private String userName;
    //真实姓名
    private String name;

    private String password;

    private Long point;

    private Byte invalid;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private SimpleAdminUserDto creator;

    private SimpleAdminUserDto modifier;

}
