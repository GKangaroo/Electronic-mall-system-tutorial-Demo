package cn.edu.xmu.oomall.service.service.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author：guiqingxin
 * @date：2022/12/20 19:38
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleServiceDto {
    private Long id;
    /**
     * 服务单类型 0上门 1寄件 2线下
     */
    private Byte type;
    /**
     * 联系人
     */
    private String consignee;
    /**
     * 服务地址
     */
    private Long serviceregionId;
    /**
     * 详细地址
     */
    private String serviceAddress;
    /**
     * 联系人电话
     */
    private String consigneeMobile;
    private Byte status;
    /**
     * 服务人员姓名
     */
    private String maintainerName;
    /**
     * 服务人员电话
     */
    private String maintainerMobile;
    /**
     * 描述
     */
    private String description;
    /**
     * 服务结果
     */
    private String result;
    /**
     * 服务商id
     */
    private Long maintainerId;//?????????
    private IdNameDto creator;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Long modifierId;
    private String modifierName;
}