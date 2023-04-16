package cn.edu.xmu.oomall.customer.mapper;

import cn.edu.xmu.oomall.customer.mapper.po.CartPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartPoMapper extends JpaRepository<CartPo,Long> {
    Page<CartPo> findByCustomerId(Long customerId, Pageable pageable);
    void deleteByCustomerId(Long customerId);
}
