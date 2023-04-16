package cn.edu.xmu.oomall.service.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author：guiqingxin
 * @date：2022/12/23 11:14
 */
@ToString(callSuper = true)
@NoArgsConstructor
public class ServiceLogistics  extends OOMallObject implements Serializable {
    private Long id;
    @Setter
    @Getter
    private Byte type;
    @Setter
    @Getter
    private Long serviceId;
    @Setter
    @Getter
    private Long packageId;
    @Builder
    public ServiceLogistics(Long id, Long creatorId, String creatorName, Long modifierId, String modifierName, LocalDateTime gmtCreate, LocalDateTime gmtModified, Byte type, Long serviceId, Long packageId){
        super(id, creatorId, creatorName, modifierId, modifierName, gmtCreate, gmtModified);
        this.type = type;
        this.serviceId = serviceId;
        this.packageId = packageId;
    }
}
