package cn.edu.xmu.oomall.service.mapper.po;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author：guiqingxin
 * @date：2022/12/23 10:59
 */
@Entity
@Data
@Table(name = "service_package")
public class ServiceLogisticsPo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private Long serviceId;
    private Long packageId;
    private Byte type;
    private Long creatorId;
    private String creatorName;
    private Long modifierId;
    private String modifierName;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
}
