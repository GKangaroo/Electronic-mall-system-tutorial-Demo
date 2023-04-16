package cn.edu.xmu.oomall.aftersale.dao.openfeign;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.oomall.aftersale.dao.bo.Customer;
import cn.edu.xmu.oomall.aftersale.dao.bo.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("customer-service")
@Repository
public interface CustomerDao {

    @GetMapping("/customer/{id}")
    InternalReturnObject<Customer> getCustomerById(@PathVariable Long id);


}
