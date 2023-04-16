package cn.edu.xmu.oomall.customer.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Builder@NoArgsConstructor
public class CartDto {

    private Long id;

    private SimpleProductDto product;

    private Integer quantity;

    private Long price;

    private List<SimpleActivityDto> activities;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

}
