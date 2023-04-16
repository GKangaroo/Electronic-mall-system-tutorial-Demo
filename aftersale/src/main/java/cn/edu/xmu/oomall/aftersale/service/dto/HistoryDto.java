package cn.edu.xmu.oomall.aftersale.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDto {

    Long id;


    Long aftersaleId;


    String content;


    LocalDateTime gmtCreate;

}
