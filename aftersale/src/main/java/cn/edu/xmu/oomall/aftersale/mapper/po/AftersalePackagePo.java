package cn.edu.xmu.oomall.aftersale.mapper.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "aftersale_package")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AftersalePackagePo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Byte type;

    private Long creatorId;

    private String creatorName;

    private Long modifierId;

    private String modifierName;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private Long aftersaleId;

    private Long packageId;
}
