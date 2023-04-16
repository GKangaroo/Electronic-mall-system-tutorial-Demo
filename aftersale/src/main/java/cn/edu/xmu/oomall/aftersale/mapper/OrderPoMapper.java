package cn.edu.xmu.oomall.aftersale.mapper;

import cn.edu.xmu.oomall.aftersale.mapper.po.HistoryPo;
import cn.edu.xmu.oomall.aftersale.mapper.po.OrderPo;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderPoMapper extends JpaRepository<OrderPo, Long> {
    Optional<OrderPo> findByOrderId(Long orderId);

}
