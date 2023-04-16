package cn.edu.xmu.oomall.aftersale.mapper;

import cn.edu.xmu.oomall.aftersale.mapper.po.AftersalePo;
import cn.edu.xmu.oomall.aftersale.mapper.po.HistoryPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryPoMapper extends JpaRepository<HistoryPo, Long> {

    Page<HistoryPo> findByAftersaleId(Long aftersaleId, Pageable pageable);
}
