package cn.edu.xmu.oomall.service.dao;

import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.service.dao.bo.ServiceLogistics;
import cn.edu.xmu.oomall.service.mapper.ServiceLogisticsMapper;
import cn.edu.xmu.oomall.service.mapper.po.ServiceLogisticsPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import static cn.edu.xmu.javaee.core.util.Common.*;
import static cn.edu.xmu.javaee.core.util.Common.cloneObj;

/**
 * @author：guiqingxin
 * @date：2022/12/23 12:42
 */
@Repository
public class ServiceLogisticsDao {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLogisticsDao.class);

    @Value("${oomall.shop.shop.timeout}")
    private long timeout;

    private RedisUtil redisUtil;

    private ServiceLogisticsMapper serviceLogisticsMapper;
    @Autowired
    public ServiceLogisticsDao(RedisUtil redisUtil, ServiceLogisticsMapper serviceLogisticsMapper) {
        this.redisUtil = redisUtil;
        this.serviceLogisticsMapper = serviceLogisticsMapper;
    }
/*    public ServiceLogistics save(ServiceLogistics serviceLogistics, UserDto userDto) throws RuntimeException{
        ServiceLogisticsPo po = cloneObj(serviceLogistics, ServiceLogisticsPo.class);
        putUserFields(po, "creator",userDto);
        putGmtFields(po, "create");
        logger.debug("saveById: po = {}", po);
        this.serviceLogisticsMapper.save(po);
        ServiceLogistics bo = cloneObj(po, ServiceLogistics.class);
        return bo;
    }*/

    public ServiceLogistics insert(ServiceLogistics serviceLogistics, UserDto userDto) throws RuntimeException{
        ServiceLogisticsPo po = cloneObj(serviceLogistics, ServiceLogisticsPo.class);
        putUserFields(po, "creator",userDto);
        putGmtFields(po, "create");
        logger.debug("saveById: po = {}", po);
        this.serviceLogisticsMapper.save(po);
        serviceLogistics.setId(po.getId());
        return cloneObj(po, ServiceLogistics.class);
    }
}
