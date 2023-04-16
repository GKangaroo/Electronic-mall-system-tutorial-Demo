package cn.edu.xmu.oomall.service.mapper;

import cn.edu.xmu.oomall.service.mapper.po.ServiceLogisticsPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author：guiqingxin
 * @date：2022/12/23 11:08
 */
@Repository
public interface ServiceLogisticsMapper extends JpaRepository<ServiceLogisticsPo, Long> {

}
