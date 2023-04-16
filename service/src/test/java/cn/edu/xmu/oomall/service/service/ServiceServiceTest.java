package cn.edu.xmu.oomall.service.service;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.model.ReturnObject;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.service.ServiceApplication;
import cn.edu.xmu.oomall.service.service.Dto.ServiceDto;
import cn.edu.xmu.oomall.service.service.Dto.SimpleServiceDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author：guiqingxin
 * @date：2022/12/25 0:29
 */
@SpringBootTest(classes = ServiceApplication.class)
@Transactional
public class ServiceServiceTest {
    @Autowired
    ServiceService serviceService;

    @MockBean
    RedisUtil redisUtil;

    UserDto user = new UserDto(1L, "test1", 0L, 1);//事实上是platform的user
    UserDto shop = new UserDto(2L, "test2", 33L, 1);//事实上是shop的user
    UserDto customer = new UserDto(3L, "test3", -100L, 2);//事实上是customer的user
    @Test
    public void retrieveServices1()
    {
        List<SimpleServiceDto> list = serviceService.retrieveServices(1L,user,1,10,null).getList();
        SimpleServiceDto dto = list.get(0);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getMaintainerId()).isEqualTo(1L);
        assertThat(dto.getResult()).isEqualTo("result");
        assertThat(dto.getType()).isEqualTo((byte) 1);
        assertThat(dto.getDescription()).isEqualTo("description");
        assertThat(dto.getConsignee()).isEqualTo("123");
        assertThat(dto.getStatus()).isEqualTo((byte)1);
        assertThat(dto.getMaintainerMobile()).isEqualTo("222");
        assertThat(dto.getMaintainerName()).isEqualTo("袋鼠2");
    }

    @Test
    public void retrieveServices2()
    {
        List<SimpleServiceDto> list = serviceService.retrieveServices(1L,user,1,10, (byte) 2).getList();
        assertThat(list.size()).isEqualTo(1);
        SimpleServiceDto dto = list.get(0);
        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getStatus()).isEqualTo((byte) 2);
        assertThat(dto.getMaintainerName()).isEqualTo("袋鼠");
    }
    @Test
    public void retrieveServices3()
    {
        List<SimpleServiceDto> list = serviceService.retrieveServices(1L,shop,1,10, (byte) 1).getList();
        assertThat(list.size()).isEqualTo(1);
        SimpleServiceDto dto = list.get(0);
        assertThat(dto.getId()).isEqualTo(1L);
    }
    @Test
    public void retrieveServices4()
    {
        List<SimpleServiceDto> list = serviceService.retrieveServices(1L,shop,1,10, null).getList();
        assertThat(list.size()).isEqualTo(1);
        SimpleServiceDto dto = list.get(0);
        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    public void findServiceById1()
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);//模拟redis中没有缓存,如果有缓存则不会调用serviceService.findServiceById
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);//这行的作用是：模拟redis中没有缓存，但是设置缓存成功

        ServiceDto retService =  serviceService.findServiceByDidAndId(1L,1L,shop);

        assertThat(retService.getId()).isEqualTo(1L);;
        assertThat(retService.getMaintainerId()).isEqualTo(1L);
        assertThat(retService.getConsignee()).isEqualTo("123");
    }

    @Test
    public void retrieveMaintainerServices()
    {
        List<SimpleServiceDto> list = serviceService.retrieveMaintainerServices(1L,shop,(byte)1,1,10).getList();
        assertThat(list.size()).isEqualTo(1);
        SimpleServiceDto dto = list.get(0);
        assertThat(dto.getDescription()).isEqualTo("description");

        List<SimpleServiceDto> list2 = serviceService.retrieveMaintainerServices(1L,shop,null,1,10).getList();
        assertThat(list2.size()).isEqualTo(2);
    }

    @Test
    public void findMaintainerServiceByDidAndId()
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);//模拟redis中没有缓存,如果有缓存则不会调用serviceService.findServiceById
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);//这行的作用是：模拟redis中没有缓存，但是设置缓存成功

        ServiceDto retService =  serviceService.findMaintainerServiceByDidAndId(1L,1L,user);

        assertThat(retService.getId()).isEqualTo(1L);;
        assertThat(retService.getMaintainerId()).isEqualTo(1L);
        assertThat(retService.getConsignee()).isEqualTo("123");

        ServiceDto retService1 =  serviceService.findMaintainerServiceByDidAndId(1L,2L,shop);
        assertThat(retService1.getStatus()).isEqualTo((byte)2);
    }

    @Test
    public void deleteServiceById1()
    {
        ReturnObject ret = serviceService.deleteServiceById(1L,1L,user);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }

    @Test
    public void deleteServiceById2()
    {
        assertThrows(BusinessException.class, ()-> serviceService.deleteServiceById(3L, 2L, user));
    }

    @Test
    public void deleteServiceById3()
    {
        assertThrows(BusinessException.class, ()-> serviceService.deleteServiceById(3L, 2L, shop));
    }

    @Test
    public void acceptService1()
    {
        ReturnObject ret = serviceService.acceptService(4L,4L,"daishu","123456",user);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }

    @Test
    public void acceptService2()
    {
        ReturnObject ret = serviceService.acceptService(4L,4L,"daishu","123456",user);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }

    @Test
    public void refuseService()
    {
        ReturnObject ret = serviceService.refuseService(4L,4L, user);
        Assertions.assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }

    @Test
    public void refuseService1()
    {
        assertThrows(BusinessException.class, ()-> serviceService.refuseService(2L,4L, user));
    }
    @Test
    public void retrieveService1()
    {
        assertThrows(BusinessException.class, ()-> serviceService.receiveService(2L,4L,true,"OK", user));
    }

    @Test
    public void retrieveService2()
    {
        assertThrows(BusinessException.class, ()-> serviceService.receiveService(2L,4L,true,"OK", user));
        ReturnObject ret = serviceService.receiveService(6L,6L,true,"OK", user);
        assertThat(ret.getCode()).isEqualTo(ReturnNo.OK);
    }

    @Test
    public void cancelService1()
    {
        assertThrows(BusinessException.class, ()-> serviceService.receiveService(6L,5L,true,"OK", user));
        ReturnObject ret = serviceService.cancelService(6L,6L,"OK", user);
    }

    @Test
    public void createService1()
    {
        SimpleServiceDto dto = serviceService.createService(8L,8L,8L, (byte) 1,"stress"
        ,"123213",1L,"sb",user);
        assertThat(dto.getMaintainerId()).isEqualTo(8L);
        assertThat(dto.getType()).isEqualTo((byte)1);
    }
}
