package cn.edu.xmu.oomall.service.dao;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.dto.PageDto;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.service.dao.bo.Service;
import cn.edu.xmu.oomall.service.dao.bo.ServiceFactory;
import cn.edu.xmu.oomall.service.mapper.ServicePoMapper;
import cn.edu.xmu.oomall.service.mapper.po.ServicePo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static cn.edu.xmu.javaee.core.util.Common.*;

/**
 * @author：guiqingxin
 * @date：2022/12/20 0:07
 */
@Repository
public class ServiceDao {
    private static final Logger logger = LoggerFactory.getLogger(ServiceDao.class);
    public static final String KEY = "S%d";
    @Value("${oomall.shop.shop.timeout}")
    private long timeout;

    private RedisUtil redisUtil;

    private ServicePoMapper servicePoMapper;

    @Autowired
    public ServiceDao(RedisUtil redisUtil, ServicePoMapper servicePoMapper) {
        this.redisUtil = redisUtil;
        this.servicePoMapper = servicePoMapper;
    }

    private Service getBo(ServicePo po, String redisKey) {
        Service bo = ServiceFactory.getServiceByPo(po);
        if (null != redisKey) {
            redisUtil.set(redisKey, bo, timeout);
        }
        return bo;
    }

    /**
     * 如果是管理员，可以看到所有的服务单
     */
    public PageDto<Service> retrieveServicesByStatus(Byte status, Integer page, Integer pageSize) throws RuntimeException{
        List<Service> ret = new ArrayList<>();
        Pageable pageable = PageRequest.of(page-1, pageSize);
        Page<ServicePo> servicePos = null;
        if(status == null){
            servicePos = servicePoMapper.findAll(pageable);
        }
        else{
            servicePos = servicePoMapper.findAllByStatus(status, pageable);
        }
        if(null != servicePos && servicePos.getSize() > 0){
            //po转bo
            ret = servicePos.stream().map(po -> cloneObj(po, Service.class)).collect(Collectors.toList());
        }
        return new PageDto<>(ret, page, pageSize);
    }

    /**
     * 店铺只能查询自己的服务单
     */
    public PageDto<Service> retrieveServicesByDidAndStatus(Long did, Byte status, Integer page, Integer pageSize) throws RuntimeException{
        List<Service> ret = new ArrayList<>();
        Pageable pageable = PageRequest.of(page-1, pageSize);
        Page<ServicePo> servicePos = null;
        //店铺，只能看到自己的服务单
        if(status == null){
            servicePos = servicePoMapper.findAllByShopId(did, pageable);
        }
        else{
            servicePos = servicePoMapper.findAllByShopIdAndStatus(did, status, pageable);
        }
        if(null != servicePos && servicePos.getSize() > 0){
            //po转bo
            ret = servicePos.stream().map(po -> getBo(po, String.format(KEY,po.getId()))).collect(Collectors.toList());
        }
        return new PageDto<>(ret, page, pageSize);
    }

    public Optional<Service> findServiceById(Long id) throws RuntimeException{
        AtomicReference<Service> service = new AtomicReference<>();
        if(null != id){
            logger.debug("findServiceById: id = " + id);
            String key = String.format(KEY,id);
            if(redisUtil.hasKey(key)){
                service.set((Service) redisUtil.get(key));
            } else {
                Optional<ServicePo> ret = servicePoMapper.findById(id);
                ret.ifPresent(po -> {
                    service.set(getBo(po, String.format(KEY,id)));
                    redisUtil.set(key, service.get(), -1);
                });
                if(null == service.get()){
                    throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "服务单", id));
                }
            }
        }
        return Optional.of(service.get());
    }

    /**
     * 商家通过这个API只能查询到自己的服务单，平台可以查询所有的
     */
    public Optional<Service> findServiceByIdAndDid(Long id, Long did) throws RuntimeException{
        AtomicReference<Service> service = new AtomicReference<>();
        if(null != id && null != did){
            logger.debug("findServiceByIdAndDid: id = " + id + ", did = " + did);
            String key = String.format(KEY,id);
            if(redisUtil.hasKey(key)){
                service.set((Service) redisUtil.get(key));
            } else {
                Optional<ServicePo> ret = servicePoMapper.findByIdAndMaintainerId(id, did);
                ret.ifPresent(po -> {
                    service.set(getBo(po,String.format(KEY,id)));
                    redisUtil.set(key, service.get(), -1);
                });
                if(null == service.get()){
                    throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "服务单", id));
                }
            }
        }
        return Optional.of(service.get());
    }

    public Set<String> save(Service service, UserDto userDto) throws RuntimeException{
        Set<String> delKey = new HashSet<>();
        if(null != service && null != service.getId()){
            String key = String.format(KEY, service.getId());
            ServicePo po = cloneObj(service, ServicePo.class);
            if(null != userDto){
                putUserFields(po,"modifier", userDto);
                putGmtFields(po, "Modified");
            }
            logger.debug("saveById:po = {}", po);
            this.servicePoMapper.save(po);
            delKey.add(key);//这一行是在：
            redisUtil.del(key);
        }
        return delKey;
    }

    public PageDto<Service> retrieveServicesByMdidAndStatus(Long did, Byte status, Integer page, Integer pageSize) {
        List<Service> ret = new ArrayList<>();
        Pageable pageable = PageRequest.of(page-1, pageSize);
        Page<ServicePo> servicePos = null;
        //服务商，只能看到自己的服务单
        if(status == null){
            servicePos = servicePoMapper.findAllByMaintainerId(did, pageable);
        }
        else{
            servicePos = servicePoMapper.findAllByMaintainerIdAndStatus(did, status, pageable);
        }
        if(null != servicePos && servicePos.getSize() > 0){
            //po转bo
            ret = servicePos.stream().map(po -> getBo(po, null)).collect(Collectors.toList());
        }
        return new PageDto<>(ret, page, pageSize);
    }

    public Optional<Service> findServiceByIdAndMaintainerId(Long id, Long did) {
        AtomicReference<Service> service = new AtomicReference<>();
        if(null != id && null != did){
            logger.debug("findServiceByIdAndMaintainerid: id = " + id + ", did = " + did);
            String key = String.format(KEY,id);
            if(redisUtil.hasKey(key)){
                service.set((Service) redisUtil.get(key));
            } else {
                Optional<ServicePo> ret = servicePoMapper.findByIdAndMaintainerId(id, did);
                ret.ifPresent(po -> {
                    service.set(getBo(po, String.format(KEY,po.getId())));
                    redisUtil.set(key, service.get(), -1);
                });
                if(null == service.get()){
                    throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST, String.format(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage(), "服务单", id));
                }
            }
        }
        return Optional.of(service.get());
    }

    public PageDto<Service> retrieveServices(Integer page, Integer pageSize) throws RuntimeException{
        List<Service> ret = new ArrayList<>();
        Pageable pageable = PageRequest.of(page-1, pageSize);
        Page<ServicePo> servicePos = servicePoMapper.findAll(pageable);
        if(null != servicePos && servicePos.getSize() > 0){
            //po转bo
            ret = servicePos.stream().map(po -> getBo(po, null)).collect(Collectors.toList());
        }
        return new PageDto<>(ret, page, pageSize);
    }

    public PageDto<Service> retrieveServicesByDid(Long did, Integer page, Integer pageSize) {
        List<Service> ret = new ArrayList<>();
        Pageable pageable = PageRequest.of(page-1, pageSize);
        Page<ServicePo> servicePos = servicePoMapper.findAllByShopId(did, pageable);
        if(null != servicePos && servicePos.getSize() > 0){
            //po转bo
            ret = servicePos.stream().map(po -> getBo(po, null)).collect(Collectors.toList());
        }
        return new PageDto<>(ret, page, pageSize);
    }

    public Service insert(Service service, UserDto userDto) {
        ServicePo po = cloneObj(service, ServicePo.class);
        putUserFields(po, "creator",userDto);
        putGmtFields(po, "create");
        logger.debug("saveById: po = {}", po);
        this.servicePoMapper.save(po);
        service.setId(po.getId());
        return cloneObj(po, Service.class);
    }


}
