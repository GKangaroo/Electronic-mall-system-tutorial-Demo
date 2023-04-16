package cn.edu.xmu.oomall.service.jpa;


import cn.edu.xmu.javaee.core.util.Common;
import cn.edu.xmu.oomall.service.ServiceApplication;
import cn.edu.xmu.oomall.service.mapper.ServiceLogisticsMapper;
import cn.edu.xmu.oomall.service.mapper.ServicePoMapper;
import cn.edu.xmu.oomall.service.mapper.po.ServicePo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author：guiqingxin
 * @date：2022/12/24 18:29
 */
@SpringBootTest(classes = ServiceApplication.class)
@Transactional
public class JpaTest {

    @Autowired
    ServicePoMapper servicePoMapper;

    @Autowired
    ServiceLogisticsMapper serviceLogisticsMapper;

    @Test
    public void JapTest(){
        ServicePo po = new ServicePo();
        po.setId(2L);
        po.setMaintainerName("袋鼠爷爷连锁店");
        Common.putGmtFields(po,"modified");
        servicePoMapper.save(po);
        Optional<ServicePo> newPo = servicePoMapper.findById(2L);
        assertThat(newPo.get().getMaintainerName().equals("袋鼠爷爷连锁店"));
        Optional<ServicePo> newPo1 = servicePoMapper.findById(1L);
        assertThat(newPo1.get().getResult().equals("result"));
    }

}