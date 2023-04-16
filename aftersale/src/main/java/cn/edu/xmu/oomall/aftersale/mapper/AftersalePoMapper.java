package cn.edu.xmu.oomall.aftersale.mapper;

import cn.edu.xmu.oomall.aftersale.dao.bo.Aftersale;
import cn.edu.xmu.oomall.aftersale.mapper.po.AftersalePo;
import cn.edu.xmu.oomall.aftersale.mapper.po.ArbitrationPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AftersalePoMapper extends JpaRepository<AftersalePo, Long> {
    Page<AftersalePo> findByCustomerIdAndStatus(Long customerId, Byte status, Pageable pageable);
    Page<AftersalePo> findByCustomerId(Long customerId, Pageable pageable);
    Page<AftersalePo> findByShopIdAndGmtCreateBetweenAndTypeAndStatus(Long shopId, LocalDateTime beginTime, LocalDateTime endTime,
                                                                      Byte type, Byte status, Pageable pageable);
    Page<AftersalePo> findByGmtCreateBetweenAndTypeAndStatus(LocalDateTime beginTime, LocalDateTime endTime,
                                                                      Byte type, Byte status, Pageable pageable);
    AftersalePo findByServiceId(Long serviceId);
    // used by Arbitration
    Page<AftersalePo> findByShopIdEquals(Long shopId, Pageable pageable);
}
