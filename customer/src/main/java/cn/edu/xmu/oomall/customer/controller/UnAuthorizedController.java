package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.service.CartService;
import cn.edu.xmu.oomall.customer.service.CustomerService;
import cn.edu.xmu.oomall.customer.controller.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class UnAuthorizedController {

    private final Logger logger = LoggerFactory.getLogger(UnAuthorizedController.class);

    private CustomerService customerService;

    private CartService cartService;

    @Autowired
    public UnAuthorizedController(CustomerService customerService, CartService cartService) {
        this.customerService = customerService;
        this.cartService = cartService;
    }

    /**
     * 注册用户
     * @author: 兰文强
     * @date: 2022/12/17 23:46
     */
    @PostMapping("/customers")
    public ReturnObject createCustomer(@Validated @RequestBody NewCustomerVo newCustomerVo) {
        return new ReturnObject(customerService.createCustomer(newCustomerVo));
    }


    /**
     * 买家查看自己信息
     * @author: 兰文强
     * @date: 2022/12/18 0:21
     */
    @GetMapping("/customers")
    @Audit
    public ReturnObject findCustomerById(@LoginUser UserDto userDto) {
        return new ReturnObject(customerService.findCustomerSelf(userDto));
    }


    /**
     * 买家修改自己信息
     * @author: 兰文强
     * @date: 2022/12/18 0:54
     */
    @PutMapping("/customers")
    @Audit
    public ReturnObject updateCustomerById(@Validated @RequestBody UpdateCustomerVo vo,
                                           @LoginUser UserDto userDto) {
        return new ReturnObject(customerService.updateCustomer(vo.getName(),vo.getMobile(),userDto));
    }

    /**
     * 用户修改密码
     * @author: 兰文强
     * @date: 2022/12/18 10:44
     */
    @PutMapping("/customers/password")
    @Audit
    public ReturnObject updatePassword(@Validated @RequestBody NewPasswordVo vo,
                                       @LoginUser UserDto userDto) {
        return new ReturnObject(customerService.updatePassword(vo.getCaptcha(),vo.getNewPassword(),userDto));
    }

    /**
     * 获取验证码
     * @author: 兰文强
     * @date: 2022/12/18 10:45
     */
    @PutMapping("/customers/password/reset")
    @Audit
    public ReturnObject findCustomerById(@Validated @RequestBody ResetPasswordVo vo,
                                         @LoginUser UserDto userDto) {
        return new ReturnObject(customerService.resetPassword(vo.getUserName(),vo.getMobile(),userDto));
    }


    /**
     * 买家登陆
     * @author: 兰文强
     * @date: 2022/12/18 10:45
     */
    @PostMapping("/customers/login")
    public ReturnObject customerLogin(@Validated @RequestBody LoginVo vo) {
        return new ReturnObject(customerService.customerLogin(vo.getUserName(),vo.getPassword()));
    }


    /**
     * 买家登出
     * @author: 兰文强
     * @date: 2022/12/18 10:45
     */
    @GetMapping("/customers/logout")
    @Audit
    public ReturnObject customerLogout(@LoginUser UserDto userDto) {
        return new ReturnObject(customerService.customerLogout(userDto));
    }

    /**
     * 买家获得购物车列表
     * @author: 兰文强
     * @date: 2022/12/18 10:45
     */
    @GetMapping("/carts")
    @Audit
    public ReturnObject retrieveCarts(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                      @LoginUser UserDto userDto) {
        return new ReturnObject(cartService.retrieveCarts(page, pageSize, userDto));
    }

    /**
     * 买家将商品加入购物车
     * @author: 兰文强
     * @date: 2022/12/18 19:31
     */
    @PostMapping("/carts")
    @Audit
    public ReturnObject createCarts(@Validated @RequestBody NewCartVo vo,
                                    @LoginUser UserDto userDto) {
        return new ReturnObject(cartService.addCart(vo.getProductId(),vo.getQuantity(),userDto));
    }


    /**
     * 买家清空购物车
     * @author: 兰文强
     * @date: 2022/12/18 19:35
     */
    @DeleteMapping("/carts")
    @Audit
    public ReturnObject deleteCarts(@LoginUser UserDto userDto) {
        return cartService.deleteCarts(userDto);
    }


    /**
     * 买家修改购物车单个商品的数量或规格
     * @author: 兰文强
     * @date: 2022/12/18 19:35
     */
    @PutMapping("/carts/{id}")
    @Audit
    public ReturnObject updateCart(@PathVariable Long id,
                                   @Validated @RequestBody NewCartVo vo,
                                   @LoginUser UserDto userDto) {
        return cartService.updateCart(id, vo.getProductId(), vo.getQuantity(), userDto);
    }



    /**
     * 买家删除购物车中商品
     * @author: 兰文强
     * @date: 2022/12/18 19:35
     */
    @DeleteMapping("/carts/{id}")
    @Audit
    public ReturnObject deleteCart(@PathVariable Long id, @LoginUser UserDto userDto) {
        return cartService.deleteCart(id,userDto);
    }




}
