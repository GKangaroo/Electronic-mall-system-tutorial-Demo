package cn.edu.xmu.oomall.aftersale.controller;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.aftersale.AftersaleApplication;
import cn.edu.xmu.oomall.aftersale.dao.AftersaleDao;
import cn.edu.xmu.oomall.aftersale.dao.bo.*;
import cn.edu.xmu.oomall.aftersale.dao.openfeign.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;


@SpringBootTest(classes = AftersaleApplication.class)
@AutoConfigureMockMvc
@Transactional
public class AftersaleControllerTest
{
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RedisUtil redisUtil;

    @MockBean
    CustomerDao customerDao;

    @MockBean
    OrderItemDao orderItemDao;

    @MockBean
    ProductDao productDao;

    @MockBean
    ServiceDao serviceDao;

    @MockBean
    ShopDao shopDao;

    static String adminToken;
    static String shopToken;
    static String userToken;
    private static final String AFTERSALES = "/aftersales";
    private static final String AFTERSALE = "/aftersales/{id}";
    private static final String ADMIN_AFTERSALE = "/shops/{id}/aftersales";
    private static final String ADMIN_AFTERSALE_SHOP = "/shops/{shopId}/aftersales/{id}";
    private static final String ADMIN_CONFIRM_AFTERSALE_SHOP = "/shops/{shopId}/aftersales/{id}/confirm";
    private static final String SHOP_CONFIRM_RECEIVE = "/shops/{shopId}/aftersales/{id}/receive";
    private static final String AFTERSALE_SENDBACK = "/aftersales/{id}/sendback";
    private static final String AFTERSALE_HISTORY="/aftersales/{id}/history";
    private static final String SHOP_AFTERSALE_SERVICE="/shops/{did}/aftersales/{id}/services";


    @BeforeAll
    public static void setup()
    {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
        shopToken = jwtHelper.createToken(1L, "shop1", 1L, 1, 3600);
        userToken = jwtHelper.createToken(1L,"user",0L,1,3600);
    }

    /**
     * 顾客查询自己的所有售后单
     *
     * @throws Exception
     */
    @Test
    public void retrieveAftersalesTest1() throws Exception
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(AFTERSALES)
                        .header("authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10")
                        .param("state", "0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(1)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    public void retrieveAftersalesTest2() throws Exception
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(AFTERSALES)
                        .header("authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].id", is(2)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 顾客通过id查询售后单
     */
    @Test
    public void retrieveAftersaleByIdTest1() throws Exception
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        InternalReturnObject<Customer> c = new InternalReturnObject<>(new Customer(1L,"user"));
        Mockito.when(customerDao.getCustomerById(Mockito.any())).thenReturn(c);
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(1L);
        InternalReturnObject<OrderItem> o = new InternalReturnObject<>(orderItem);
        Mockito.when(orderItemDao.getOrderItemById(Mockito.any())).thenReturn(o);
        InternalReturnObject<Product> p = new InternalReturnObject<>(new Product());
        Mockito.when(productDao.getProductById(Mockito.any())).thenReturn(p);
        InternalReturnObject<Shop> s = new InternalReturnObject<>(new Shop());
        Mockito.when(shopDao.getShopById(Mockito.any())).thenReturn(s);

        this.mockMvc.perform(MockMvcRequestBuilders.get(AFTERSALE, 1)
                        .header("authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(1)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 顾客修改售后单信息（只能在申请态status=NEW）
     */
    @Test
    public void updateAftersaleByIdTest1() throws Exception
    {
        String body = "{\"quantity\":1,\"reason\":\"Reason.\",\"consignee\":{\"name\":\"John\",\"mobile\":\"123\",\"regionId\":1,\"address\":\"test\"}}";
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.put(AFTERSALE, 1)
                        .header("authorization", userToken)
                        .content(body.getBytes("utf-8"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void updateAftersaleByIdTest2() throws Exception
    {
        String body = "{\"quantity\":1,\"reason\":\"Reason.\",\"consignee\":{\"name\":\"John\",\"mobile\":\"123\",\"regionId\":1,\"address\":\"test\"}}";
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.put(AFTERSALE, 2)
                        .header("authorization", userToken)
                        .content(body.getBytes("utf-8"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 买家取消售后单（只能在申请和处理态取消）
     */
    @Test
    public void deleteAftersaleByIdTest1() throws Exception
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(AFTERSALE, 2)
                        .header("authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.STATENOTALLOW.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteAftersaleByIdTest2() throws Exception
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(AFTERSALE, 1)
                        .header("authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 管理员查看所有售后单
     */
    @Test
    public void retrieveAdminAftersaleByIdTest1() throws Exception
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_AFTERSALE, 1)
                        .header("authorization", shopToken)
                        .param("page", "1")
                        .param("pageSize", "10")
                        .param("type", "0")
                        .param("status", "0")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(1)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].type", is(0)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].status", is(0)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();

    }

    /**
     * 管理员查询某店铺中某id的售后单
     */
    @Test
    public void retrieveAdminByShopAftersaleTest1() throws Exception
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        InternalReturnObject<Customer> c = new InternalReturnObject<>(new Customer(1L,"user"));
        Mockito.when(customerDao.getCustomerById(Mockito.any())).thenReturn(c);
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(1L);
        InternalReturnObject<OrderItem> o = new InternalReturnObject<>(orderItem);
        Mockito.when(orderItemDao.getOrderItemById(Mockito.any())).thenReturn(o);
        InternalReturnObject<Product> p = new InternalReturnObject<>(new Product());
        Mockito.when(productDao.getProductById(Mockito.any())).thenReturn(p);
        InternalReturnObject<Shop> s = new InternalReturnObject<>(new Shop());
        Mockito.when(shopDao.getShopById(Mockito.any())).thenReturn(s);

        this.mockMvc.perform(MockMvcRequestBuilders.get(ADMIN_AFTERSALE_SHOP, 1, 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.shop.id", is(1)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 管理员审核售后单（退换货维修）
     */
    @Test
    public void updateAdminConfirmAftersaleTest1() throws Exception
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"confirm\":true,\"conclusion\":\"Conclusion.\",\"type\":0}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADMIN_CONFIRM_AFTERSALE_SHOP, 1, 1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(body.getBytes("utf-8")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 店家验收买家的退换货（维修不可调）
     */
    @Test
    public void updateShopConfirmReceiveTest1() throws Exception
    {
        String body = "{\"confirm\":true,\"conclusion\":\"Conclusion.\",\"serialNo\":\"111\"}";
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.put(SHOP_CONFIRM_RECEIVE, 1, 1)
                        .header("authorization", shopToken)
                        .content(body.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void updateShopConfirmReceiveTest2() throws Exception {
        String body = "{\"confirm\":true,\"conclusion\":\"Conclusion.\",\"serialNo\":\"111\"}";
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.put(SHOP_CONFIRM_RECEIVE, 1, 2)
                .header("authorization", shopToken)
                .content(body.getBytes("utf-8"))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", Matchers.is(ReturnNo.AFTERSALE_NOT_RETURNCHANGE.getErrNo())))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

//这个API已经被老师删除
//    /**
//     * 买家填写售后的运单信息
//     */
//    @Test
//    public void updateAftersaleSendbackTest1() throws Exception
//    {
//        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
//        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
//        String body = "{\"logSn\":\"test\"}";
//        this.mockMvc.perform(MockMvcRequestBuilders.put(AFTERSALE_SENDBACK, 1)
//                        .header("authorization", userToken)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(body.getBytes("utf-8")))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//    }
//
//    @Test
//    public void updateAftersaleSendbackTest2() throws Exception
//    {
//        String body = "{\"logSn\":\"test\"}";
//        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
//        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
//        this.mockMvc.perform(MockMvcRequestBuilders.put(AFTERSALE_SENDBACK, 9)
//                        .header("authorization", userToken)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(body.getBytes("utf-8")))
//                .andExpect(MockMvcResultMatchers.status().isForbidden())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errno",  Matchers.is(ReturnNo.AFTERSALE_NOT_SELFSENDBACK.getErrNo())))
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//    }

    /**
     * 买家通过售后单id查询售后轨迹
     */
    @Test
    public void retrieveHistoryByIdTest1() throws Exception
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(AFTERSALE_HISTORY, 1)
                        .header("authorization", userToken)
                        .param("page","1")
                        .param("pageSize","10")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].aftersaleId",is(1)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 店铺查询售后服务单信息
     */
    @Test
    public void retrieveShopAftersaleByIdTest1() throws Exception
    {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        InternalReturnObject<Service> service = new InternalReturnObject<>(new Service());
        Mockito.when(serviceDao.getServiceById(Mockito.any())).thenReturn(service);

        this.mockMvc.perform(MockMvcRequestBuilders.get(SHOP_AFTERSALE_SERVICE, 1,1)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

}
