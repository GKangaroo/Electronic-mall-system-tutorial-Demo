package cn.edu.xmu.oomall.aftersale.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleArbitrationDto {

    Long id;

    int status;

    String reason;

    String result;

    LocalDateTime gmtarbitration;

    LocalDateTime gmtaccept;

    int applicantType;

    LocalDateTime gmtCreate;

}
