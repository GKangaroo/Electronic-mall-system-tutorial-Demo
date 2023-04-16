package cn.edu.xmu.oomall.aftersale.mapper.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "aftersale_arbitration")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArbitrationPo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long aftersaleId;

    private String reason;

    private String result;

    private Long creatorId;

    private String creatorName;

    private Long modifierId;

    private String modifierName;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Long arbitratorId;

    private String arbitratorName;

    private LocalDateTime gmtarbitration;

    private int applicantType;

    private LocalDateTime gmtaccept;

    private Byte status;

}
