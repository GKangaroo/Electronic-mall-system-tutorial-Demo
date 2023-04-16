package cn.edu.xmu.oomall.aftersale.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleAftersaleDto {

    Long id;

    String aftersaleSn;

    Byte type;

    String conclusion;

    Long quantity;

    /// refund

    Byte status;

    Byte inArbitrated;

}
