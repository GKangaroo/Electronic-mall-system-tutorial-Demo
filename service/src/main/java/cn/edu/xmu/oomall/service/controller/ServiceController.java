package cn.edu.xmu.oomall.service.controller;

import cn.edu.xmu.javaee.core.aop.Audit;
import cn.edu.xmu.javaee.core.aop.LoginUser;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.StatusDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.service.controller.vo.ServiceConfirmVo;
import cn.edu.xmu.oomall.service.controller.vo.ServiceInfoVo;
import cn.edu.xmu.oomall.service.controller.vo.ServiceReceiveVo;
import cn.edu.xmu.oomall.service.controller.vo.ServiceResultVo;
import cn.edu.xmu.oomall.service.dao.bo.Service;
import cn.edu.xmu.oomall.service.service.Dto.SimpleServiceDto;
import cn.edu.xmu.oomall.service.service.ServiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * @author：guiqingxin
 * @date：2022/12/19 9:28
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class ServiceController {
    private final Logger logger = LoggerFactory.getLogger(ServiceController.class);

    private ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }
    /**
    获得服务单的所有状态
    */
    @GetMapping("/services/states")
    @Audit(departName = "shops")
    public ReturnObject retrieveServiceStates(){
        return new ReturnObject(Service.STATUSNAMES.keySet().stream().map(key -> new StatusDto(key, Service.STATUSNAMES.get(key))).collect(Collectors.toList()));
    }

    /**
     * 店铺查询服务单信息
     * 商家通过这个API只能查询到自己的服务单，平台可以查询所有的
     * @param did
     * @param userDto
     * @param status
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/shops/{did}/services")
    @Audit(departName = "shops")
    public ReturnObject retrieveAllServices(@PathVariable("did") Long did, @LoginUser UserDto userDto,
                                         @RequestParam(required = false) Byte status,
                                         @RequestParam(required = false,defaultValue = "1") Integer page,
                                         @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        return new ReturnObject(serviceService.retrieveServices(did,userDto,page,pageSize,status));
    }
    /**
     * 商家通过这个API只能查询到自己的服务单，平台可以查询所有的
     */
    @GetMapping("/shops/{did}/services/{id}")
    @Audit(departName = "shops")
    public ReturnObject findServiceById(@PathVariable("did") Long did, @PathVariable("id") Long id,
                                        @LoginUser UserDto userDto){
        return new ReturnObject(serviceService.findServiceByDidAndId(did, id, userDto));
    }
    /**
     * 商家只能取消自己的服务单，平台可以取消所有的
     * @param did
     * @return
     */
    @DeleteMapping("/shops/{did}/services/{id}")
    @Audit(departName = "shops")
    public ReturnObject deleteServiceById(@PathVariable("did") Long did,
                                          @PathVariable("id") Long id,
                                          @LoginUser UserDto userDto){
        return new ReturnObject(serviceService.deleteServiceById(did, id, userDto));
    }

    @GetMapping("/maintainers/{did}/services")
    @Audit(departName = "shops")
    public ReturnObject retrieveMaintainerServices(@PathVariable("did") Long did,
                                                   @LoginUser UserDto userDto,
                                                   @RequestParam(required = false) Byte status,
                                                   @RequestParam(required = false,defaultValue = "1") Integer page,
                                                   @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        return new ReturnObject(serviceService.retrieveMaintainerServices(did, userDto, status, page, pageSize));
    }

    @GetMapping("/maintainers/{did}/services/{id}")
    @Audit(departName = "shops")
    public ReturnObject findMaintainerServiceById(@PathVariable("did") Long did,
                                                  @PathVariable("id") Long id,
                                                  @LoginUser UserDto userDto){
        return new ReturnObject(serviceService.findMaintainerServiceByDidAndId(did, id, userDto));
    }

    /**
     * 服务商接受服务单信息
     * @param did
     * @param id
     * @param userDto
     * @param vo
     * @return
     */
    @PutMapping("/maintainers/{did}/services/{id}/accept")
    @Audit(departName = "shops")
    public ReturnObject acceptService(@PathVariable("did") Long did,
                                      @PathVariable("id") Long id,
                                      @LoginUser UserDto userDto,
                                      @RequestBody ServiceConfirmVo vo){
        ReturnObject ret = new ReturnObject();
        if(vo.getConfirm()){
            ret = serviceService.acceptService(did, id, vo.getMaintainerName(),vo.getMaintainerMobile(), userDto);
        }
        return new ReturnObject(ret);
    }

    @PutMapping("/maintainers/{did}/services/{id}/refuse")
    @Audit(departName = "shops")
    public ReturnObject refuseService(@PathVariable("did") Long did,
                                      @PathVariable("id") Long id,
                                      @LoginUser UserDto userDto
                                      ){
        return new ReturnObject(serviceService.refuseService(did, id, userDto));
    }

    @PutMapping("/maintainers/{did}/services/{id}/receive")
    @Audit(departName = "shops")
    public ReturnObject receiveService(@PathVariable("did") Long did,
                                       @PathVariable("id") Long id,
                                       @LoginUser UserDto userDto,
                                       @RequestBody ServiceReceiveVo vo){
        return new ReturnObject(serviceService.receiveService(did, id, vo.getAccepted(),vo.getResult(), userDto));
    }

    @PutMapping("/maintainers/{did}/service/{id}/finish")
    @Audit(departName = "shops")
    public ReturnObject finishService(@PathVariable("did") Long did,
                                  @PathVariable("id") Long id,
                                  @LoginUser UserDto userDto,
                                  @RequestBody ServiceResultVo vo){
        return new ReturnObject(serviceService.finishService(did, id, vo.getResult(),userDto));
    }

    @PutMapping("/maintainers/{did}/service/{id}/cancel")
    @Audit(departName = "shops")
    public ReturnObject cancelService(@PathVariable("did") Long did,
                                      @PathVariable("id") Long id,
                                      @LoginUser UserDto userDto,
                                      @RequestBody ServiceResultVo vo){
        return new ReturnObject(serviceService.cancelService(did, id, vo.getResult() , userDto));
    }
    /**
     * 创建服务单
     * @param shopId
     * @param id
     * @param mid
     * @param userDto
     * @return
     */
    @PostMapping("/internal/shops/{shopId}/orderitems/{id}/maintainers/{mid}")
    @Audit(departName = "shops")
    public ReturnObject createService(@PathVariable("shopId") Long shopId,
                                      @PathVariable("id") Long id,
                                      @PathVariable("mid") Long mid,
                                      @LoginUser UserDto userDto,
                                      @RequestBody ServiceInfoVo vo){
        logger.debug("createService: shopId = {}, id = {}, mid = {}, vo = {}", shopId, id, mid, vo);
        SimpleServiceDto simpleServiceDto = serviceService.createService(shopId, id, mid, vo.getType(),vo.getConsignee().getAddress(),vo.getConsignee().getMobile()
                ,vo.getConsignee().getRegionId(),vo.getConsignee().getName(),userDto);
        return new ReturnObject(ReturnNo.CREATED, simpleServiceDto);
    }
}
