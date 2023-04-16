package cn.edu.xmu.oomall.service.dao;

import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.service.ServiceApplication;
import cn.edu.xmu.oomall.service.dao.bo.Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author：guiqingxin
 * @date：2022/12/25 13:25
 */
@SpringBootTest(classes = ServiceApplication.class)
@Transactional
public class ServiceDaoTest {
    @MockBean
    RedisUtil redisUtil;

    @Autowired
    ServiceDao serviceServiceDao;

    @Test
    public void retrieveServicesByStatus()//Byte status, Integer page, Integer pageSize
    {
        List<Service> list = serviceServiceDao.retrieveServicesByStatus(null,1,10).getList();
        assertThat(list.size()).isEqualTo(2);
        Service bo = list.get(0);
        assertThat(bo.getId()).isEqualTo(1L);
        assertThat(bo.getResult()).isEqualTo("result");
    }
}
