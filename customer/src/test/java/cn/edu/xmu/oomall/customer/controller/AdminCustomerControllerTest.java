package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.customer.CustomerApplication;
import cn.edu.xmu.oomall.customer.dao.bo.CouponActivity;
import cn.edu.xmu.oomall.customer.dao.openFeign.CouponActivityDao;
import cn.edu.xmu.oomall.customer.dao.openFeign.OnsaleDao;
import cn.edu.xmu.oomall.customer.dao.openFeign.OrderItemDao;
import cn.edu.xmu.oomall.customer.dao.openFeign.ProductDao;
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
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = CustomerApplication.class)
@AutoConfigureMockMvc
@Transactional
public class AdminCustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RedisUtil redisUtil;

    @MockBean
    CouponActivityDao couponActivityDao;

    @MockBean
    ProductDao productDao;

    @MockBean
    OnsaleDao onsaleDao;

    @MockBean
    OrderItemDao orderItemDao;

    static String adminToken;

    static String shopToken;

    private static final String SHOP_CUSTOMERS = "/shops/{shopId}/customers";
    private static final String SHOP_CUSTOMER = "/shops/{shopId}/customers/{id}";
    private static final String SHOP_CUSTOMER_BAN= "/shops/{did}/customers/{id}/ban";
    private static final String SHOP_CUSTOMER_RELEASE = "/shops/{did}/customers/{id}/release";

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "admin", 0L, 1, 3600);
        shopToken = jwtHelper.createToken(15L, "shop1", 1L, 1, 3600);
    }

    @Test
    public void retrieveCustomers1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(SHOP_CUSTOMERS,0)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("userName", "699275")
                        .param("name", "赵永波")
                        .param("mobile", "13159235540")
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].userName", is("699275")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void retrieveCustomers2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(SHOP_CUSTOMERS,1)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("userName", "699275")
                        .param("name", "赵永波")
                        .param("mobile", "13159235540")
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", Matchers.is(ReturnNo.AUTH_NO_RIGHT.getErrNo())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void retrieveCustomers3() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(SHOP_CUSTOMERS,1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("name", "赵永波")
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].userName", is("699275")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void retrieveCustomers4() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(SHOP_CUSTOMERS,1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("userName", "699275")
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].userName", is("699275")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void retrieveCustomers5() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(SHOP_CUSTOMERS,1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].userName", is("699275")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[1].userName", is("105048")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findCustomerById1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(SHOP_CUSTOMER,0,1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userName", is("699275")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("赵永波")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.gmtCreate", is("2021-12-21T10:41:48")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.creator.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.creator.userName", is("admin")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findCustomerById2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(SHOP_CUSTOMER,1,1)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", Matchers.is(ReturnNo.AUTH_NO_RIGHT.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteCustomerById1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(SHOP_CUSTOMER,0,1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteCustomerById2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(SHOP_CUSTOMER,1,1)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", Matchers.is(ReturnNo.AUTH_NO_RIGHT.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void banCustomerById1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.put(SHOP_CUSTOMER_BAN,0,1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void banCustomerById2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.put(SHOP_CUSTOMER_BAN,1,1)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", Matchers.is(ReturnNo.AUTH_NO_RIGHT.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void releaseCustomerById1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.put(SHOP_CUSTOMER_RELEASE,0,1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void releaseCustomerById2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.put(SHOP_CUSTOMER_RELEASE,1,1)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", Matchers.is(ReturnNo.AUTH_NO_RIGHT.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }


}











