package cn.edu.xmu.oomall.customer.mapper;

import cn.edu.xmu.oomall.customer.mapper.po.CustomerPo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerPoMapper extends JpaRepository<CustomerPo,Long> {
    CustomerPo findByUserName(String userName);
    CustomerPo findByMobile(String mobile);
    List<CustomerPo> findByName(String mobile);
    Page<CustomerPo> findByUserNameAndNameAndMobile(String userName, String name, String mobile, Pageable pageable);
    Page<CustomerPo> findByUserNameAndName(String userName, String name, Pageable pageable);
    Page<CustomerPo> findByUserNameAndMobile(String userName,String mobile, Pageable pageable);
    Page<CustomerPo> findByNameAndMobile(String name, String mobile, Pageable pageable);
    Page<CustomerPo> findByUserName(String userName, Pageable pageable);
    Page<CustomerPo> findByMobile(String mobile, Pageable pageable);
    Page<CustomerPo> findByName(String name, Pageable pageable);




}
