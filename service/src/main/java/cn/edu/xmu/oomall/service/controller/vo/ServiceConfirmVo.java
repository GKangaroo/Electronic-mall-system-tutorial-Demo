package cn.edu.xmu.oomall.service.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author：guiqingxin
 * @date：2022/12/23 0:24
 */
@Data
@NoArgsConstructor
public class ServiceConfirmVo {
    Boolean confirm;
    String maintainerName;
    String maintainerMobile;
}
