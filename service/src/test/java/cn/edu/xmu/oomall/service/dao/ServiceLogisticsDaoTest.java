package cn.edu.xmu.oomall.service.dao;

import cn.edu.xmu.oomall.service.ServiceApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author：guiqingxin
 * @date：2022/12/24 20:31
 */
@SpringBootTest(classes = ServiceApplication.class)
@Transactional
public class ServiceLogisticsDaoTest {
    @Autowired
    ServiceLogisticsDao serviceLogisticsDao;

    /*@Test
    public void save(){
        UserDto user = new UserDto();
        user.setId(2L);
        user.setName("test1");
        user.setUserLevel(1);
        ServiceLogistics bo = new ServiceLogistics();
        bo.setServiceId(1L);
        bo.setType((byte) 1);

        ServiceLogistics retBo = serviceLogisticsDao.insert(bo,user);
        assertThat(retBo.getServiceId()).isEqualTo(bo.getServiceId());
        assertThat(retBo.getType()).isEqualTo(bo.getType());
    }*/
}
