package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.service.SuccessShareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class SuccessShareController {

    private final Logger logger = LoggerFactory.getLogger(SuccessShareController.class);

    private SuccessShareService successshareService;

    @Autowired
    public SuccessShareController(SuccessShareService successshareService) {
        this.successshareService= successshareService;
    }

    /**
     * 顾客查询分享成功记录
     */
    @GetMapping("/shares/{id}/success")
    @Audit
    public ReturnObject retrieveSuccessShares(@PathVariable Long id,
                                              @RequestParam(required = false) LocalDateTime beginTime,
                                              @RequestParam(required = false) LocalDateTime endTime,
                                              @RequestParam(required = false) Integer page,
                                              @RequestParam(required = false) Integer pageSize,
                                              @LoginUser UserDto userDto) {
        return new ReturnObject(successshareService.retrieveSuccessShares(id, beginTime, endTime, page, pageSize, userDto));
    }

    /**
     * 管理员查询所有分享成功记录
     */
    @GetMapping("/shops/{did}/onsales/{id}/successshares")
    @Audit(departName = "shops")
    public ReturnObject AdminRetrieveSuccessShares(@PathVariable Long did,
                                                   @PathVariable Long id,
                                                   @RequestParam(required = false) LocalDateTime beginTime,
                                                   @RequestParam(required = false) LocalDateTime endTime,
                                                   @RequestParam(required = false) Integer page,
                                                   @RequestParam(required = false) Integer pageSize,
                                                   @LoginUser UserDto userDto) {
        return new ReturnObject(successshareService.AdminRetrieveSuccessShares(did, id, beginTime, endTime, page, pageSize, userDto));
    }

}
