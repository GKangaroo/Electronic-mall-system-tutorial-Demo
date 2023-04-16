package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.customer.service.ShareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class ShareController {

    private final Logger logger = LoggerFactory.getLogger(ShareController.class);

    private ShareService shareService;

    @Autowired
    public ShareController(ShareService shareService) {
        this.shareService= shareService;
    }

    /**
     * 顾客查询所有分享记录
     */
    @GetMapping("/shares")
    @Audit
    public ReturnObject retrieveShares(@RequestParam(required = false) Long productId,
                                        @RequestParam(required = false) LocalDateTime beginTime,
                                        @RequestParam(required = false) LocalDateTime endTime,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer pageSize,
                                        @LoginUser UserDto userDto) {
        return new ReturnObject(shareService.retrieveShares(productId, beginTime, endTime, page, pageSize, userDto));
    }

    /**
     * 顾客查看商品的详细信息（需登录，从分享模式查看商品）
     */
    @GetMapping("/shares/{sid}/products/{id}")
    @Audit
    public ReturnObject retrieveProducts(@PathVariable Long sid,
                                         @PathVariable Long id,
                                         @LoginUser UserDto userDto) {
        return new ReturnObject(shareService.retrieveProducts(sid, id, userDto));
    }

    /**
     * 管理员查询商品分享记录
     */
    @GetMapping("/shops/{did}/products/{id}/shares")
    @Audit(departName = "shops")
    public ReturnObject AdminRetrieveShares(@PathVariable Long did,
                                            @PathVariable Long id,
                                            @RequestParam(required = false) Integer page,
                                            @RequestParam(required = false) Integer pageSize,
                                            @LoginUser UserDto userDto) {
        return new ReturnObject(shareService.AdminRetrieveShares(did, id, page, pageSize, userDto));
    }

}
