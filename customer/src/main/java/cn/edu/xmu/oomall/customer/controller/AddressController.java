package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.controller.vo.*;
import cn.edu.xmu.oomall.customer.service.AddressService;
import cn.edu.xmu.oomall.customer.controller.vo.NewAddressVo;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class AddressController {

    private final Logger logger = LoggerFactory.getLogger(AddressController.class);

    private AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * 买家新增地址
     */
    @PostMapping("/addresses")
    @Audit
    public ReturnObject createAddresses(@Validated @RequestBody @NotNull NewAddressVo vo,
                                        @LoginUser UserDto userDto) {
        return new ReturnObject(addressService.addAddresses(vo.getRegionId(), vo.getAddress(), vo.getConsignee(), vo.getMobile(), userDto));
    }

    /**
     * 买家查询所有已有的地址信息
     */
    @GetMapping("/addresses")
    @Audit
    public ReturnObject retrieveAddresses(@RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer pageSize,
                                      @LoginUser UserDto userDto) {
        return new ReturnObject(addressService.retrieveAddresses(page, pageSize, userDto));
    }

    /**
     * 买家设置默认地址
     */
    @PutMapping("/addresses/{id}/default")
    @Audit
    public ReturnObject setDefaultAddresses(@PathVariable Long id,
                                            @LoginUser UserDto userDto) {
        return new ReturnObject(addressService.setDefaultAddresses(id, userDto));
    }

    /**
     * 买家修改自己的地址信息
     */
    @PutMapping("/addresses/{id}")
    @Audit
    public ReturnObject updateAddresses(@PathVariable Long id,
                                        @Validated @RequestBody @NotNull NewAddressVo vo,
                                        @LoginUser UserDto userDto) {
        return new ReturnObject(addressService.updateAddresses(id, vo.getRegionId(), vo.getAddress(), vo.getConsignee(), vo.getMobile(), userDto));
    }

    /**
     * 买家删除地址
     */
    @DeleteMapping("/addresses/{id}")
    @Audit
    public ReturnObject deleteAddresses(@PathVariable Long id,
                                        @LoginUser UserDto userDto) {
        return new ReturnObject(addressService.deleteAddresses(id, userDto));
    }


}

