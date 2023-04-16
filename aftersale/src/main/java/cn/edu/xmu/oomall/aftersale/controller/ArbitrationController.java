package cn.edu.xmu.oomall.aftersale.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.oomall.aftersale.controller.vo.*;
import cn.edu.xmu.oomall.aftersale.dao.bo.Arbitration;
import cn.edu.xmu.oomall.aftersale.service.ArbitrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class ArbitrationController {

    private final Logger logger = LoggerFactory.getLogger(ArbitrationController.class);

    private ArbitrationService arbitrationService;

    @Autowired
    public ArbitrationController(ArbitrationService arbitrationService) {
        this.arbitrationService = arbitrationService;
    }

    /*
     * 查询仲裁单的全部状态
     *
     * */
    @GetMapping("/arbitrations/states")
    public ReturnObject getAllArbitrationStates() {
        List<StateVo> ret = new ArrayList<>();
        ret.add(new StateVo(Arbitration.APPLYING, Arbitration.STATUSNAMES.get(Arbitration.APPLYING)));
        ret.add(new StateVo(Arbitration.ACCEPT, Arbitration.STATUSNAMES.get(Arbitration.ACCEPT)));
        ret.add(new StateVo(Arbitration.CANCEL, Arbitration.STATUSNAMES.get(Arbitration.CANCEL)));
        ret.add(new StateVo(Arbitration.CLOSED, Arbitration.STATUSNAMES.get(Arbitration.CLOSED)));
        // 临时使用
//        JwtHelper jwtHelper = new JwtHelper();
//        String adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 86400);
//        logger.info("test token = {}", adminToken);
        return new ReturnObject(ret);
    }

    /*
     * 用户申请售后仲裁
     * */
    @PostMapping("aftersales/{id}/arbitrations")
    @Audit
    public ReturnObject createArbitration(@PathVariable("id") Long id,
                                          @RequestBody @Validated NewArbitrationVo vo,
                                          @LoginUser UserDto userDto) {
        return new ReturnObject(arbitrationService.createArbitration(id, vo.getType(), vo.getQuantity(), vo.getReason(), vo.getRegionId(), vo.getDetail(), vo.getConsignee(), vo.getMobile(), userDto));
    }

    /*
     * 仲裁员查询申请的仲裁单信息
     *
     * */
    @GetMapping("/shops/{shopId}/arbitrations")
    @Audit(departName = "admin")
    public ReturnObject findUnacceptedArbitrations(@PathVariable("shopId") Long shopId,
                                                   @RequestParam(required = false) Integer page,
                                                   @RequestParam(required = false) Integer pageSize,
                                                   @LoginUser UserDto userDto) {
        return new ReturnObject(arbitrationService.retrieveUnacceptedArbitrations(page, pageSize, userDto));
    }

    /*
     * 仲裁员查询自己负责的仲裁单信息
     *
     * */
    @GetMapping("/shops/{shopId}/arbitrations/self")
    @Audit(departName = "shops")
    public ReturnObject findArbitration(@PathVariable("shopId") Long shopId,
                                        @RequestParam(required = false) Integer page,
                                        @RequestParam(required = false) Integer pageSize,
                                        @LoginUser UserDto userDto) {
        return new ReturnObject(arbitrationService.findArbitrationsByArbitratorId(page, pageSize, userDto.getId(), userDto));
    }

    /*
     * 根据仲裁id查询仲裁单详细信息
     *
     * */
    @GetMapping("/shops/{shopId}/arbitrations/{id}")
    @Audit
    public ReturnObject findArbitrationById(@PathVariable("shopId") Long shopId,
                                            @PathVariable("id") Long id,
                                            @LoginUser UserDto userDto) {
        return new ReturnObject(arbitrationService.findById(id, userDto));
    }

    /*
     * 申请人取消仲裁
     *
     * */
    @DeleteMapping("/shops/{shopId}/arbitrations/{id}")
    @Audit
    public ReturnObject deleteArbitrationById(@PathVariable("shopId") Long shopId,
                                              @PathVariable("id") Long id,
                                              @LoginUser UserDto userDto) {
        return arbitrationService.deleteById(id, userDto);
    }

    /*
     * 仲裁员受理仲裁单
     *
     * */
    @PutMapping("/shops/{shopId}/arbitrations/{id}/accept")
    @Audit(departName = "admin")
    public ReturnObject acceptArbitrations(@PathVariable("shopId") Long shopId,
                                           @PathVariable("id") Long arbitrationId,
                                           @RequestBody @Validated ArbitratorVo arbitrator,
                                           @LoginUser UserDto userDto) {
        return new ReturnObject(arbitrationService.acceptById(arbitrationId, arbitrator.getArbitrationId(), arbitrator.getArbitrationName(), userDto));
    }

    /*
     * 仲裁员仲裁结案
     *
     * */
    @PutMapping("/shops/{shopId}/arbitrations/{id}/close")
    @Audit(departName = "admin")
    public ReturnObject closeArbitrations(@PathVariable("shopId") Long shopId,
                                          @PathVariable("id") Long arbitrationId,
                                          @RequestBody CloseResultVo result,
                                          @LoginUser UserDto userDto) {
        return arbitrationService.closeById(arbitrationId, result.getResult(), userDto);

    }
}
