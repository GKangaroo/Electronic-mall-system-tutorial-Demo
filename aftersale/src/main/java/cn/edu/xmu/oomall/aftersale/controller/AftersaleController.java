package cn.edu.xmu.oomall.aftersale.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.aftersale.controller.vo.*;
import cn.edu.xmu.oomall.aftersale.dao.bo.Aftersale;
import cn.edu.xmu.oomall.aftersale.service.AftersaleService;
import cn.edu.xmu.oomall.aftersale.service.dto.ConsigneeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class AftersaleController {
    private final Logger logger = LoggerFactory.getLogger(AftersaleController.class);

    private AftersaleService aftersaleService;

    @Autowired
    public AftersaleController(AftersaleService aftersaleService) {
        this.aftersaleService = aftersaleService;
    }


    /**
     * 顾客查询所有自己的售后单
     *
     * @author: 兰文强
     * @date: 2022/12/22 15:03
     */
    @GetMapping("/aftersales")
    @Audit
    public ReturnObject retrieveAftersales(@RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                           @RequestParam(required = false) Byte state,
                                           @LoginUser UserDto userDto) {
        return new ReturnObject(aftersaleService.retrieveAftersales(state, page, pageSize, userDto));
    }


    /**
     * 顾客查询自己的售后单详情
     *
     * @param id
     * @return
     */
    @GetMapping("/aftersales/{id}")
    @Audit
    public ReturnObject findAftersaleById(@PathVariable(value = "id", required = true) Long id,
                                          @LoginUser UserDto userDto) {
        return new ReturnObject(aftersaleService.findAftersaleById(id, userDto));
    }


    /**
     * 顾客修改售后单信息（只能在申请态）
     *
     * @param id
     * @return
     */
    @PutMapping("/aftersales/{id}")
    @Audit
    public ReturnObject updateAftersaleById(@PathVariable(value = "id") Long id,
                                            @Validated @RequestBody UpdateAfterSaleVo vo,
                                            @LoginUser UserDto userDto) {
        return aftersaleService.updataAftersale(id, vo.getQuantity(), vo.getReason(), vo.getConsignee(), userDto);
    }


    /**
     * 买家取消售后单 只可以在申请和处理中的状态取消
     *
     * @param id
     * @return
     */
    @DeleteMapping("/aftersales/{id}")
    @Audit
    public ReturnObject deleteAftersaleById(@PathVariable(value = "id", required = true) Long id,
                                            @LoginUser UserDto userDto) {
        return aftersaleService.deleteAftersale(id, userDto);
    }


    /**
     * 管理员查看所有售后单（可根据售后类型和状态选择）
     *
     * @param id
     * @return
     */
    @GetMapping("/shops/{id}/aftersales")
    @Audit(departName = "shops")
    public ReturnObject retrieveAllAftersales(@PathVariable(value = "id", required = true) Long id,
                                              @RequestParam(required = false) LocalDateTime beginTime,
                                              @RequestParam(required = false) LocalDateTime endTime,
                                              @RequestParam(required = false, defaultValue = "1") Integer page,
                                              @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                              @RequestParam(required = false) Byte type,
                                              @RequestParam(required = false) Byte state,
                                              @LoginUser UserDto userDto) {

        return new ReturnObject(aftersaleService.retrieveAllAftersales(
                id, beginTime, endTime, page, pageSize, type, state, userDto));
    }


    /**
     * 管理员根据售后单id查询售后单信息
     *
     * @param id
     * @return
     */
    @GetMapping("/shops/{shopId}/aftersales/{id}")
    @Audit(departName = "shops")
    public ReturnObject findAftersalesById(@PathVariable(value = "shopId") Long shopId,
                                           @PathVariable(value = "id") Long id,
                                           @LoginUser UserDto userDto) {
        return new ReturnObject(aftersaleService.findAftersaleById(shopId, id, userDto));
    }


    /**
     * "管理员审核同意/不同意（退款，换货，维修）"
     *
     * @param id
     * @return
     */
    @PutMapping("/shops/{shopId}/aftersales/{id}/confirm")
    @Audit(departName = "shops")
    public ReturnObject confirmAftersales(@PathVariable(value = "shopId") Long shopId,
                                          @PathVariable(value = "id") Long id,
                                          @RequestBody @Validated ConfirmVo vo,
                                          @LoginUser UserDto userDto) {
        return aftersaleService.confirmAftersales(shopId, id, vo.isConfirm(), vo.getConclusion(), vo.getType(), userDto);
    }


    /**
     * 店家验收买家的退（换）货 维修不可调用此API
     *
     * @param id
     * @return
     */
    @PutMapping("/shops/{shopId}/aftersales/{id}/receive")
    @Audit(departName = "shops")
    public ReturnObject shopConfirmReceive(@PathVariable(value = "shopId") Long shopId,
                                           @PathVariable(value = "id") Long id,
                                           @RequestBody @Validated ReceiveVo vo,
                                           @LoginUser UserDto userDto) {
        return aftersaleService.shopConfirmReceive(shopId, id, vo.isConfirm(), vo.getConclusion(), vo.getSerialNo(), userDto);
    }

    //这个API，已经被老师删了
//    /**
//     * 买家填写售后的运单信息
//     * 在处理中的状态，且售后关联的售后物流为顾客自行发件的前提下才可以调用此API
//     */
//    @PutMapping("/aftersales/{id}/sendback")
//    @Audit
//    public ReturnObject sendbackAftersale(@PathVariable Long id,
//                                          @RequestBody @Validated SendBackVo vo,
//                                          @LoginUser UserDto userDto) {
//        return aftersaleService.sendbackAftersale(id, vo.getLogSn(), userDto);
//    }


    /**
     * 通过售后单id查询售后轨迹
     *
     * @param id
     * @return
     */
    @GetMapping("/aftersales/{id}/history")
    @Audit
    public ReturnObject retrieveHistory(@PathVariable Long id,
                                        @RequestParam Integer page,
                                        @RequestParam Integer pageSize,
                                        @LoginUser UserDto userDto) {
        return new ReturnObject(aftersaleService.retrieveHistory(id, page, pageSize, userDto));
    }


    /**
     * 店铺查询售后的服务单信息
     *
     * @param id
     * @return
     */
    @GetMapping("/shops/{did}/aftersales/{id}/services")
    @Audit(departName = "shops")
    public ReturnObject getAfterService(@PathVariable Long did,
                                        @PathVariable Long id,
                                        @LoginUser UserDto userDto) {
        return new ReturnObject(aftersaleService.retrieveService(did, id, userDto));
    }

    /**
     * 买家提交售后单
     */
    @PostMapping("/orderitems/{id}/aftersales")
    @Audit
    public ReturnObject createAftersales(@PathVariable("id") Long id,
                                         @RequestBody @Validated NewAftersaleVo newAftersaleVo,
                                         @LoginUser UserDto userDto) {
        return new ReturnObject(aftersaleService
                .createAftersale(id,
                        newAftersaleVo.getType(),
                        newAftersaleVo.getQuantity(),
                        newAftersaleVo.getReason(),
                        cloneObj(newAftersaleVo.getConsignee(), ConsigneeDto.class),
                        userDto));
    }

    /**
     * 顾客查询所有的可申请售后的订单明细
     */
    @GetMapping("/aftersales-orderitems")
    @Audit
    public ReturnObject getAftersalesOrderitems(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @LoginUser UserDto userDto) {
        return new ReturnObject(aftersaleService.retriveOrderItemsByCustomerId(page, pageSize, userDto));
    }

    /*
     * 查询售后单的全部状态
     *
     * */
    @GetMapping("/aftersales/states")
    public ReturnObject getAllAftersalesStates() {
        List<StateVo> ret = new ArrayList<>();
        ret.add(new StateVo(Aftersale.NEW, Aftersale.STATUSNAMES.get(Aftersale.NEW)));
        ret.add(new StateVo(Aftersale.PROCESS, Aftersale.STATUSNAMES.get(Aftersale.PROCESS)));
        ret.add(new StateVo(Aftersale.DISPATCHING, Aftersale.STATUSNAMES.get(Aftersale.DISPATCHING)));
        ret.add(new StateVo(Aftersale.REFUNDING, Aftersale.STATUSNAMES.get(Aftersale.REFUNDING)));
        ret.add(new StateVo(Aftersale.REPLACING, Aftersale.STATUSNAMES.get(Aftersale.REPLACING)));
        ret.add(new StateVo(Aftersale.END, Aftersale.STATUSNAMES.get(Aftersale.END)));
        ret.add(new StateVo(Aftersale.CANCEL, Aftersale.STATUSNAMES.get(Aftersale.CANCEL)));
        return new ReturnObject(ret);
    }


}
