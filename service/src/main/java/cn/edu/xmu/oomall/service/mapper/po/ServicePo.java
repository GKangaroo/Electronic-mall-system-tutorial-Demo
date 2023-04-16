package cn.edu.xmu.oomall.service.mapper.po;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author：guiqingxin
 * @date：2022/12/22 16:46
 */
@Entity
@Data
@Table(name = "service_service")
public class ServicePo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;

    private Long maintainerId;

    private Long shopId;
    private String result;
    private Byte type;
    private String description;
    private Long regionId;
    private String address;
    private String consignee;
    private String mobile;
    private Byte status;
    private String maintainerMobile;
    private String maintainerName;
    private Long creatorId;
    private String creatorName;
    private Long modifierId;
    private String modifierName;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
