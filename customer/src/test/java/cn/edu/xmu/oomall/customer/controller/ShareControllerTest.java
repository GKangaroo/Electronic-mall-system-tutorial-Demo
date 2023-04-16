package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.customer.CustomerApplication;
import cn.edu.xmu.oomall.customer.dao.bo.Onsale;
import cn.edu.xmu.oomall.customer.dao.bo.Product;
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

import static org.hamcrest.CoreMatchers.is;


@SpringBootTest(classes = CustomerApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ShareControllerTest {

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

    private static final String SHARES = "/shares";
    private static final String SHARES_PRODUCTS = "/shares/{sid}/products/{id}";
    private static final String SHOP_SHARES = "/shops/{did}/products/{id}/shares";

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "admin", 0L, 1, 3600);
        shopToken = jwtHelper.createToken(4L, "shop1", 0L, 1, 3600);
        customerToken = jwtHelper.createToken(13960L,"78677",1L,1,3600);
    }

    @Test
    public void retrieveShares() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(onsaleDao.getOnsaleById(Mockito.any())).thenReturn(new InternalReturnObject<>(new Onsale()));
        this.mockMvc.perform(MockMvcRequestBuilders.get(SHARES)
                        .header("authorization", customerToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(24574)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].customer.id", is(13960)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].onsale.id", is(39)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].quantity", is(44)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].gmtCreate", is("2021-12-21T14:59:23")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveProducts() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(productDao.getProductById(Mockito.any())).thenReturn(new InternalReturnObject<>(new Product()));
        this.mockMvc.perform(MockMvcRequestBuilders.get(SHARES_PRODUCTS,24574,39)
                        .header("authorization", customerToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(24574)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.quantity", is(44)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void AdminRetrieveShares() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(onsaleDao.retrieveOnsales(Mockito.anyLong(),Mockito.anyLong())).thenReturn(new InternalReturnObject<>(new Onsale()));
        this.mockMvc.perform(MockMvcRequestBuilders.get(SHOP_SHARES,4,1588)
                        .header("authorization", shopToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(10)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(24574)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].customer.id", is(13960)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].onsale.id", is(39)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].quantity", is(44)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].gmtCreate", is("2021-12-21 14:59:23")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }


}
