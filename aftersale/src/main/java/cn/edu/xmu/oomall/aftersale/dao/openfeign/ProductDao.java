package cn.edu.xmu.oomall.aftersale.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.aftersale.dao.bo.Product;
import cn.edu.xmu.oomall.aftersale.service.dto.ProductNameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("goods-service")
@Repository
public interface ProductDao {

    @GetMapping("/product/{id}")
    InternalReturnObject<Product> getProductById(@PathVariable Long id);

}
