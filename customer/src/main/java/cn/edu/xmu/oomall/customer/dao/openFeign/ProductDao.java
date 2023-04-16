package cn.edu.xmu.oomall.customer.dao.openFeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.customer.dao.bo.Onsale;
import cn.edu.xmu.oomall.customer.dao.bo.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("tmp3-service")
@Repository
public interface ProductDao {

    //用于在分享模式下浏览商品
    @GetMapping("/product/{id}")
    InternalReturnObject<Object> getProductById(@PathVariable Long id);

    //用于购物车查询简要的商品信息
    @GetMapping("/product/{id}")
    InternalReturnObject<Product> getSimpleProductById(@PathVariable Long id);





}
