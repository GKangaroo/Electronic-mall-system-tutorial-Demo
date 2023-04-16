package cn.edu.xmu.oomall.shop.service.dto;

import cn.edu.xmu.oomall.shop.dao.openfeign.bo.Product;
import cn.edu.xmu.oomall.shop.dao.openfeign.bo.Region;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author chenyz
 * @date 2022-11-26 22:42
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleProductServiceDto {
    private Long id;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Byte invalid;
    private Integer priority;
    private Product product;
    private SimpleShopDto shop;
    private Region region;
}
