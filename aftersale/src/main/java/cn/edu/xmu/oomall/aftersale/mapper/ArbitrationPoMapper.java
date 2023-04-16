package cn.edu.xmu.oomall.aftersale.mapper;

import cn.edu.xmu.oomall.aftersale.mapper.po.ArbitrationPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArbitrationPoMapper extends JpaRepository<ArbitrationPo, Long> {

    Page<ArbitrationPo> findByAftersaleIdEquals(Long aftersaleId, Pageable pageable);

    ArbitrationPo findByIdEquals(Long id);

    Page<ArbitrationPo> findByArbitratorIdEquals(Long arbitratorId, Pageable pageable);

    Page<ArbitrationPo> findByStatusEquals(Byte status, Pageable pageable);

    ArbitrationPo save(ArbitrationPo po);
}
