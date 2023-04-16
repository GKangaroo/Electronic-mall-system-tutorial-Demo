package cn.edu.xmu.oomall.aftersale.dao.bo;

import lombok.Data;

@Data
public class OrderItem {

    private Long id;

    private Long productId;

    private Long orderId;

    private String name;

    private Integer quantity;

    private Integer price;

    private Integer discountPrice;

}
