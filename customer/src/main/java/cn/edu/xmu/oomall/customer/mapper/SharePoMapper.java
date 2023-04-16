package cn.edu.xmu.oomall.customer.mapper;

import cn.edu.xmu.oomall.customer.mapper.po.SharePo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SharePoMapper extends JpaRepository<SharePo,Long> {
    Page<SharePo> findByCustomerIdAndGmtCreateBetween(Long customerId, LocalDateTime beginTime, LocalDateTime endTime, Pageable pageable);
    Page<SharePo> findByOnsaleIdAndCustomerIdAndGmtCreateBetween(Long onsaleId,Long customerId, LocalDateTime beginTime, LocalDateTime endTime, Pageable pageable);
    Page<SharePo> findByOnsaleIdAndCustomerId(Long onsaleId,Long customerId, Pageable pageable);
    Page<SharePo> findByCustomerId(Long customerId, Pageable pageable);
    Page<SharePo> findByOnsaleId(Long onsaleId, Pageable pageable);
}
