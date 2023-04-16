package cn.edu.xmu.oomall.customer.mapper;

import cn.edu.xmu.oomall.customer.mapper.po.AddressPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressPoMapper extends JpaRepository<AddressPo,Long> {
    List<AddressPo> findByCustomerId(Long customerId);

    Optional<AddressPo> findByBeDefault(Integer beDefault);
}
