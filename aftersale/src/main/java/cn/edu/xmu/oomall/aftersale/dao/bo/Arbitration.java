package cn.edu.xmu.oomall.aftersale.dao.bo;

import cn.edu.xmu.javaee.core.model.bo.OOMallObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Arbitration extends OOMallObject implements Serializable {

    Long id;

    Byte status;

    String reason;

    String result;

    Long aftersaleId;

    Long arbitratorId;

    String arbitratorName;

    LocalDateTime gmtarbitration;

    LocalDateTime gmtaccept;

    int applicantType;

    Long creatorId;

    String creatorName;

    LocalDateTime gmtCreate;

    LocalDateTime gmtModified;

    Long modifierId;

    String modifierName;

    @JsonIgnore
    @ToString.Exclude
    public static final Byte APPLYING = 0;

    @JsonIgnore
    @ToString.Exclude
    public static final Byte ACCEPT = 1;

    @JsonIgnore
    @ToString.Exclude
    public static final Byte CANCEL = 2;

    @JsonIgnore
    @ToString.Exclude
    public static final Byte CLOSED = 3;

    @JsonIgnore
    @ToString.Exclude
    public static final Map<Byte, String> STATUSNAMES = new HashMap(){
        {
            put(APPLYING, "申请");
            put(ACCEPT, "受理");
            put(CANCEL, "撤销");
            put(CLOSED, "结案");
        }
    };
}
