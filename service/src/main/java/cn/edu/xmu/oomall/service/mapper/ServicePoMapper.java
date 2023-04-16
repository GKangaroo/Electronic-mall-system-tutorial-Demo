package cn.edu.xmu.oomall.service.mapper;

import cn.edu.xmu.oomall.service.mapper.po.ServicePo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author：guiqingxin
 * @date：2022/12/20 20:36
 */
@Repository
public interface ServicePoMapper extends JpaRepository<ServicePo, Long> {
    Page<ServicePo> findAll(Pageable pageable);

    Page<ServicePo> findAllByStatus(Byte status, Pageable pageable);

    Page<ServicePo> findAllByShopIdAndStatus(Long did, Byte status, Pageable pageable);

    Page<ServicePo> findAllByShopId(Long did, Pageable pageable);

    Optional<ServicePo> findByIdAndShopId(Long id, Long did);

    Page<ServicePo> findAllByMaintainerId(Long did, Pageable pageable);

    Page<ServicePo> findAllByMaintainerIdAndStatus(Long did, Byte status, Pageable pageable);

    Optional<ServicePo> findByIdAndMaintainerId(Long id, Long did);
}
