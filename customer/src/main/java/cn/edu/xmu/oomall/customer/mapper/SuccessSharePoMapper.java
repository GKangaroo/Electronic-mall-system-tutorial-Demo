package cn.edu.xmu.oomall.customer.mapper;

import cn.edu.xmu.oomall.customer.mapper.po.SuccessSharePo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SuccessSharePoMapper extends JpaRepository<SuccessSharePo,Long> {
    Page<SuccessSharePo> findByShareId(Long shareId, Pageable pageable);
    Page<SuccessSharePo> findByShareIdAndGmtCreateBetween(Long shareId, LocalDateTime beginTime, LocalDateTime endTime, Pageable pageable);


}
