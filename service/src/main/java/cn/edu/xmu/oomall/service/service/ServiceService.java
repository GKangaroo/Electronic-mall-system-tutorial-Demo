package cn.edu.xmu.oomall.service.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.oomall.service.dao.ServiceDao;
import cn.edu.xmu.oomall.service.dao.ServiceLogisticsDao;
import cn.edu.xmu.oomall.service.dao.bo.Service;
import cn.edu.xmu.oomall.service.service.Dto.ServiceDto;
import cn.edu.xmu.oomall.service.service.Dto.SimpleServiceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.model.Constants.PLATFORM;
import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

/**
 * @author：guiqingxin
 * @date：2022/12/19 9:56
 */
@org.springframework.stereotype.Service
public class ServiceService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceService.class);
    public static final String KEY = "S%d";
    private ServiceDao serviceDao;
    private ServiceLogisticsDao serviceLogisticsDao;
    @Autowired
    public ServiceService(ServiceDao serviceDao, ServiceLogisticsDao serviceLogisticsDao) {
        this.serviceDao = serviceDao;
        this.serviceLogisticsDao = serviceLogisticsDao;
    }
    /**
     * 商家通过这个API只能查询到自己的服务单，平台可以查询所有的
     */
    public PageDto<SimpleServiceDto> retrieveServices(Long did, UserDto userDto, Integer page, Integer pageSize, Byte status) {
        PageDto<Service> servicePageDto = null;
        List<SimpleServiceDto> ret = new ArrayList<>();
        //如果是管理员，可以看到所有的服务单
        if(PLATFORM == userDto.getDepartId()){
            //取回所有的服务单BO
            if(status == null){
            servicePageDto = serviceDao.retrieveServices(page, pageSize);
            }
            else servicePageDto = serviceDao.retrieveServicesByStatus(status, page, pageSize);
        }
        else {
            //取回本部门的服务单
            if(status == null){
                servicePageDto = serviceDao.retrieveServicesByDidAndStatus(did, status, page, pageSize);
            }
            else servicePageDto = serviceDao.retrieveServicesByDid(did, page, pageSize);
        }
        if(null != servicePageDto && servicePageDto.getList().size() > 0){
            ret = servicePageDto.getList().stream().map(po -> cloneObj(po,SimpleServiceDto.class)).collect(Collectors.toList());
        }
        return new PageDto<>(ret, page, pageSize);
    }

    public ServiceDto findServiceByDidAndId(Long did, Long id, UserDto userDto) {
        Optional<Service> ret = serviceDao.findServiceById(id);
        Service service = ret.orElse(null);
        if(PLATFORM != userDto.getDepartId() && did != service.getShopId()){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "服务商", did, service.getMaintainerId()));
        }
        if(did == service.getShopId()){
            ret = serviceDao.findServiceByIdAndDid(id, did);
        }
        if(ret.isPresent()){
            Service servicebo = ret.get();
            /*IdNameDto creator = (null == service.getCreatorId())?null: IdNameDto.builder().id(service.getCreatorId()).name(service.getCreatorName()).build();
            IdNameDto modifier = (null == service.getModifierId())?null: IdNameDto.builder().id(service.getModifierId()).name(service.getModifierName()).build();
            商铺模块的写法*/
            return cloneObj(servicebo, ServiceDto.class);
        }else return null;
    }

    /**
     * 根据id删除服务单
     * @param status
     * @return
     */
    public ReturnObject updateServiceStatus(Service service, Byte status, UserDto userDto) {
        if (service.allowStatus(status)) {
            service.setStatus(status);
            serviceDao.save(service, userDto);
        } else {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "商铺", service.getShopId(), service.getStatusName()));
        }
        return new ReturnObject(ReturnNo.OK);
    }

    /*public ReturnObject updateServiceStatusByDid(Long id, Long did, Byte status, ServiceBo service) {
        Optional<ServiceBo> ret = serviceDao.findServiceByIdAndDid(id, did);
        ServiceBo service = ret.orElse(null);
        if (service.allowStatus(status)) {
            service.setStatus(status);
            serviceDao.save(service, userDto);
        } else {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "服务商", id, service.getStatusName()));
        }
        return new ReturnObject(ReturnNo.OK);
    }*/
    public ReturnObject deleteServiceById(Long did, Long id, UserDto userDto) {
        Optional<Service> ret = serviceDao.findServiceById(id);
        Service service = ret.orElse(null);
        //如果是管理员，可以删除所有的服务单
        if(PLATFORM == userDto.getDepartId()){
            return this.updateServiceStatus(service, Service.CANCEL, userDto);
        }
        else if(did == service.getShopId()){
            //取回本部门的服务单
            return this.updateServiceStatus(service, Service.CANCEL, userDto);
        }
        else {
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "商铺", did, service.getMaintainerId()));
        }
    }

    public PageDto<SimpleServiceDto> retrieveMaintainerServices(Long did, UserDto userDto, Byte status, Integer page, Integer pageSize) {
        PageDto<Service> servicePageDto = serviceDao.retrieveServicesByMdidAndStatus(did, status, page, pageSize);
        List<SimpleServiceDto> ret = new ArrayList<>();
        if(null != servicePageDto && servicePageDto.getList().size() > 0){
            ret = servicePageDto.getList().stream().map(po -> cloneObj(po,SimpleServiceDto.class)).collect(Collectors.toList());
        }
        return new PageDto<>(ret, page, pageSize);
    }

    public ServiceDto findMaintainerServiceByDidAndId(Long did, Long id, UserDto userDto) {
        Optional<Service> ret = null;
        if(PLATFORM == userDto.getDepartId()){
            ret = serviceDao.findServiceById(id);
        }
        else {
            ret = serviceDao.findServiceByIdAndDid(id, did);
        }
        if(ret.isPresent()){
            Service service = ret.get();

            return cloneObj(service, ServiceDto.class);
        }
        return null;
    }

    public ReturnObject acceptService(Long did, Long id,  String maintainerName, String maintainerMobile, UserDto userDto) {
        //先用id与did取回服务单
        Optional<Service> ret = serviceDao.findServiceById(id);//findServiceByIdAndMaintainerId(id, did);
        Service service = ret.orElse(null);
        if(!PLATFORM.equals(userDto.getDepartId()) && !did.equals(service.getMaintainerId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "服务商", did, service.getMaintainerId()));
        }

        service.accept(maintainerName,maintainerMobile,userDto);

        return new ReturnObject();
    }

    public ReturnObject refuseService(Long did, Long id, UserDto userDto) {
        //先用id与did取回服务单
        Optional<Service> ret = serviceDao.findServiceByIdAndMaintainerId(id, did);
        Service service = ret.orElse(null);
        if(PLATFORM != userDto.getDepartId() && !did.equals(service.getMaintainerId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "服务商", did, service.getMaintainerId()));
        }
        if (service.allowStatus(Service.CANCEL)) {
            service.setStatus(Service.CANCEL);
            serviceDao.save(service, userDto);
        } else {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "服务商", id, service.getStatusName()));
        }
        return new ReturnObject(ReturnNo.OK);
    }

    public ReturnObject receiveService(Long did, Long id, Boolean accepted, String result, UserDto userDto) {
        //先用id与did取回服务单
        Optional<Service> ret = serviceDao.findServiceByIdAndMaintainerId(id, did);
        Service service = ret.orElse(null);
        if(PLATFORM != userDto.getDepartId() && !did.equals(service.getMaintainerId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "服务商", did, service.getMaintainerId()));
        }
        if(accepted) {
            if (service.allowStatus(Service.REPAIR)) {
                service.setStatus(Service.REPAIR);
                service.setResult(result);
                serviceDao.save(service, userDto);
            } else {
                throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "服务商", id, service.getStatusName()));
            }
        }
        return new ReturnObject(ReturnNo.OK);
    }

    public ReturnObject finishService(Long did, Long id, String result, UserDto userDto) {
        //先用id与did取回服务单
        Optional<Service> ret = serviceDao.findServiceByIdAndMaintainerId(id, did);
        Service service = ret.orElse(null);
        if(PLATFORM != did && !did.equals(service.getMaintainerId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "服务商", did, service.getMaintainerId()));
        }
        service.finish(result,userDto);
        return new ReturnObject(ReturnNo.OK);
    }

    public ReturnObject cancelService(Long did, Long id, String result, UserDto userDto) {
        //先用id与did取回服务单
        Optional<Service> ret = serviceDao.findServiceByIdAndMaintainerId(id, did);
        Service service = ret.orElse(null);
        if(PLATFORM != did && !did.equals(service.getMaintainerId())){
            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE, String.format(ReturnNo.RESOURCE_ID_OUTSCOPE.getMessage(), "服务商", did, service.getMaintainerId()));
        }
        if (service.allowStatus(Service.CANCEL)) {
            service.setStatus(Service.CANCEL);
            service.setResult(result);
            serviceDao.save(service, userDto);
        } else {
            throw new BusinessException(ReturnNo.STATENOTALLOW, String.format(ReturnNo.STATENOTALLOW.getMessage(), "服务商", id, service.getStatusName()));
        }
        return new ReturnObject(ReturnNo.OK);
    }
    //创建服务单
    public SimpleServiceDto createService(Long shopId, Long id, Long mid, Byte type, String address, String mobile, Long regionId, String name, UserDto userDto) {
        Service service = Service.builder()
                .shopId(shopId)
                .maintainerId(mid)
                .type(type)
                .serviceAddress(address)
                .consigneeMobile(mobile)
                .serviceregionId(regionId)
                .consignee(name)
                .build();
        Service serviceBo = serviceDao.insert(service, userDto);
        return cloneObj(serviceBo, SimpleServiceDto.class);
    }
}
