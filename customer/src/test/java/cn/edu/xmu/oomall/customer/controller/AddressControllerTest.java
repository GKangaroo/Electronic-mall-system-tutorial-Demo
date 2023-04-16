package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.customer.CustomerApplication;
import cn.edu.xmu.oomall.customer.dao.openFeign.CouponActivityDao;
import cn.edu.xmu.oomall.customer.dao.openFeign.OnsaleDao;
import cn.edu.xmu.oomall.customer.dao.openFeign.OrderItemDao;
import cn.edu.xmu.oomall.customer.dao.openFeign.ProductDao;
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

import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = CustomerApplication.class)
@AutoConfigureMockMvc
@Transactional
public class AddressControllerTest {

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

    static String customerToken;

    private static final String ADDRESSES = "/addresses";
    private static final String ADDRESSES_DEFAULT = "/addresses/{id}/default";
    private static final String ADDRESS = "/addresses/{id}";

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "admin", 0L, 1, 3600);
        shopToken = jwtHelper.createToken(15L, "shop1", 1L, 1, 3600);
        customerToken = jwtHelper.createToken(1L,"699275",1L,1,3600);
    }

    @Test
    public void createAddresses1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"regionId\":\"2417\",\"address\":\"人民北路\",\"consignee\":\"赵永波\",\"mobile\":\"13959235540\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(ADDRESSES)
                        .header("authorization", customerToken)
                        .content(body.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.regionId", is(2417)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.address", is("人民北路")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.consignee", is("赵永波")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.mobile", is("13959235540")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }


    @Test
    public void retrieveAddresses() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(ADDRESSES)
                        .header("authorization", customerToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].regionId", is(2417)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].consignee", is("赵永波")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].mobile", is("13959235540")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void setDefaultAddresses() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADDRESSES_DEFAULT,2417)
                        .header("authorization", customerToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void updateAddresses() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"regionId\":\"2417\",\"address\":\"人民北路\",\"consignee\":\"赵永波\",\"mobile\":\"13959235540\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(ADDRESS,2417)
                        .header("authorization", customerToken)
                        .content(body.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteAddresses() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(ADDRESS,2417)
                        .header("authorization", customerToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(4)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }


}
