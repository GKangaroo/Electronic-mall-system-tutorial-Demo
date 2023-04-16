package cn.edu.xmu.oomall.customer.mapper;

import cn.edu.xmu.oomall.customer.mapper.po.CouponPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponPoMapper extends JpaRepository<CouponPo,Long> {
    Page<CouponPo> findByUsed(Integer used, Pageable pageable);
    Page<CouponPo> findByActivityId(Long activityId, Pageable pageable);
    Page<CouponPo> findByActivityIdAndUsed(Long activityId, Integer used, Pageable pageable);

    Long countByActivityIdEquals(Long activityId);
    Optional<CouponPo> findByActivityIdEqualsAndCustomerIdEquals(Long activityId,Long customerId);
}
