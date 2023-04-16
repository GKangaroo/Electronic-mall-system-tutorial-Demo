package cn.edu.xmu.oomall.service.controller.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author：guiqingxin
 * @date：2022/12/19 19:30
 */
@Data
@NoArgsConstructor
public class ServiceInfoVo {
    /**
     * 服务单类型 0上门 1寄件 2线下
     */
    Byte type;
    ServiceConsigneeVo consignee;
}
