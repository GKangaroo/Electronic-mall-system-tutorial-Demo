package cn.edu.xmu.oomall.aftersale.dao.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//用于消息服务器
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    //订单id
    private Long id;

    //订单状态
    private Byte state;

}
