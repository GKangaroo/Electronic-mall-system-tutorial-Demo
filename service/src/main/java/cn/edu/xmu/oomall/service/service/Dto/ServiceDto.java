package cn.edu.xmu.oomall.service.service.Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author：guiqingxin
 * @date：2022/12/21 10:10
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceDto {
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
    private String serviceaddress;
    /**
     * 联系人电话
     */
    private String consigneemobile;
    private Byte status;
    /**
     * 服务人员姓名
     */
    private String maintainerMobile;
    private String maintainerName;
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
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private IdNameDto creator;
    private IdNameDto modifier;
}
