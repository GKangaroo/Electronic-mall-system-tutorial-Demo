package cn.edu.xmu.oomall.service.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author：guiqingxin
 * @date：2022/12/19 21:38
 */
@Data
@NoArgsConstructor
public class ServiceVo {
    Byte type;
    String name;
    String mobile;
    Long regionId;
    String address;
}
