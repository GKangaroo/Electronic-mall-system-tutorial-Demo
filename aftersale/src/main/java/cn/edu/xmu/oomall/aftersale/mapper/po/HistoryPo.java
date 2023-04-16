package cn.edu.xmu.oomall.aftersale.mapper.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "aftersale_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryPo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long aftersaleId;


    private String content;


    private Long creatorId;


    private String creatorName;


    private Long modifierId;


    private String modifierName;


    private LocalDateTime gmtCreate;


    private LocalDateTime gmtModified;
}
