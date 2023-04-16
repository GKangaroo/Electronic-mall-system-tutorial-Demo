package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;

@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/shops/{shopId}", produces = "application/json;charset=UTF-8")
public class AdminCustomerController {

    private final Logger logger = LoggerFactory.getLogger(AdminCustomerController.class);

    private CustomerService customerService;

    @Autowired
    public AdminCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 平台管理员获取所有用户列表
     * @author: 兰文强
     * @date: 2022/12/18 19:39
     */
    @GetMapping("/customers")
    @Audit(departName = "shops")
    public ReturnObject retrieveCustomers(@PathVariable Long shopId,
                                          @RequestParam(required = false) String userName,
                                          @RequestParam(required = false) String name,
                                          @RequestParam(required = false) String mobile,
                                          @RequestParam(required = false) Integer page,
                                          @RequestParam(required = false) Integer pageSize,
                                          @LoginUser UserDto userDto){
        if(!PLATFORM.equals(userDto.getDepartId())){
            return new ReturnObject(ReturnNo.AUTH_NO_RIGHT);
        }
        return new ReturnObject(customerService.retrieveCustomers(shopId, userName, name, mobile, page, pageSize, userDto));
    }

    /**
     * 平台管理员查看任意买家信息
     * @author: 兰文强
     * @date: 2022/12/18 19:43
     */
    @GetMapping("/customers/{id}")
    @Audit(departName = "shops")
    public ReturnObject findCustomerById(@PathVariable Long shopId,
                                          @PathVariable Long id,
                                          @LoginUser UserDto userDto){
        if(!PLATFORM.equals(userDto.getDepartId())){
            return new ReturnObject(ReturnNo.AUTH_NO_RIGHT);
        }
        return new ReturnObject(customerService.findCustomerById(id));
    }

    /**
     * 管理员查看逻辑删除顾客
     * @author: 兰文强
     * @date: 2022/12/18 19:43
     */
    @DeleteMapping("/customers/{id}")
    @Audit(departName = "shops")
    public ReturnObject deleteCustomerById(@PathVariable Long shopId,
                                          @PathVariable Long id,
                                          @LoginUser UserDto userDto){
        if(!PLATFORM.equals(userDto.getDepartId())){
            return new ReturnObject(ReturnNo.AUTH_NO_RIGHT);
        }
        return customerService.deleteCustomer(shopId, id, userDto);
    }


    /**
     * 平台管理员封禁买家
     * @author: 兰文强
     * @date: 2022/12/18 19:43
     */
    @PutMapping("/customers/{id}/ban")
    @Audit(departName = "shops")
    public ReturnObject banCustomerById(@PathVariable Long shopId,
                                          @PathVariable Long id,
                                          @LoginUser UserDto userDto){
        if(!PLATFORM.equals(userDto.getDepartId())){
            return new ReturnObject(ReturnNo.AUTH_NO_RIGHT);
        }
        return customerService.banCustomer(shopId, id, userDto);
    }


    /**
     * 平台管理员解禁买家
     * @author: 兰文强
     * @date: 2022/12/18 19:43
     */
    @PutMapping("/customers/{id}/release")
    @Audit(departName = "shops")
    public ReturnObject releaseCustomerById(@PathVariable Long shopId,
                                          @PathVariable Long id,
                                          @LoginUser UserDto userDto){
        if(!PLATFORM.equals(userDto.getDepartId())){
            return new ReturnObject(ReturnNo.AUTH_NO_RIGHT);
        }
        return customerService.releaseCustomer(shopId, id, userDto);
    }



}



