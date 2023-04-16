package cn.edu.xmu.oomall.aftersale.service.dto;

import cn.edu.xmu.oomall.aftersale.dao.bo.Arbitration;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArbitrationDto {

    Long id;

    Byte status;

    String reason;

    String result;

    Long aftersaleId;

    SimpleAdminUserDto arbitrator;

    LocalDateTime gmtarbitration;

    LocalDateTime gmtaccept;

    int applicantType;

    SimpleAdminUserDto creator;

    LocalDateTime gmtCreate;

    LocalDateTime gmtModified;

    SimpleAdminUserDto modifier;

    public ArbitrationDto getUser(Arbitration bo) {
        arbitrator = new SimpleAdminUserDto(bo.getArbitratorId(), bo.getArbitratorName());
        creator = new SimpleAdminUserDto(bo.getCreatorId(), bo.getCreatorName());
        modifier = new SimpleAdminUserDto(bo.getModifierId(), bo.getModifierName());
        return this;
    }

}
