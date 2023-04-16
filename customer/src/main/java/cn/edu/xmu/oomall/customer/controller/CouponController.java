package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class CouponController {

    private final Logger logger = LoggerFactory.getLogger(CouponController.class);

    private CouponService couponService;

    @Autowired
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    /**
     * 顾客查看优惠卷列表
     */
    @GetMapping("/coupons")
    @Audit
    public ReturnObject retrieveCoupons(@RequestParam(required = false) Integer status,
                                        @RequestParam(required = false) Long actId,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer pageSize,
                                        @LoginUser UserDto userDto) {
        return new ReturnObject(couponService.retrieveCoupons(status, actId, page, pageSize, userDto));
    }

    /**
     * 买家领取活动优惠券，上线状态才能领取
     */
    @PostMapping("/couponActivities/{id}/coupons")
    @Audit
    public ReturnObject receiveCoupons(@PathVariable Long id,
                                       @LoginUser UserDto userDto) {
        return new ReturnObject(couponService.getCouponById(id, userDto));
    }

//    // 用于测试高并发大负载的设计
//    @PostMapping("/couponActivities/{id}/coupons/{type}")
//    @Audit
//    public ReturnObject receiveCoupons(@PathVariable Long id,
//                                       @PathVariable Long type,
//                                       @LoginUser UserDto userDto) {
//        return new ReturnObject(couponService.receiveCouponsT(id, type, userDto.getId()));
//    }
}
