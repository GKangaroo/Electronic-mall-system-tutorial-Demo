package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.customer.CustomerApplication;
import cn.edu.xmu.oomall.customer.dao.bo.Onsale;
import cn.edu.xmu.oomall.customer.dao.bo.OrderItem;
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
public class SuccessShareControllerTest {

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

    private static final String SUCCESS_SHARES = "/shares/{id}/success";
    private static final String ONSALES_SUCCESSSHARES = "/shops/{did}/onsales/{id}/successshares";

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "admin", 0L, 1, 3600);
        shopToken = jwtHelper.createToken(15L, "shop1", 1L, 1, 3600);
        customerToken = jwtHelper.createToken(14723L,"875315",1L,1,3600);
    }

    @Test
    public void retrieveSuccessShares() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(orderItemDao.getOrderItemById(Mockito.any())).thenReturn(new InternalReturnObject<>(new OrderItem()));
        Mockito.when(productDao.getProductById(Mockito.any())).thenReturn(new InternalReturnObject<>(new Product()));
        Product product = new Product(3274L,"测试商品",114514L,123,null);
        Mockito.when(productDao.getSimpleProductById(Mockito.any())).thenReturn(new InternalReturnObject<>(product));
        Mockito.when(couponActivityDao.getCouponActivityByProductId(Mockito.anyLong())).thenReturn(new InternalReturnObject<>());
        Mockito.when(onsaleDao.getOnsaleById(Mockito.any())).thenReturn(new InternalReturnObject<>(new Onsale()));
        this.mockMvc.perform(MockMvcRequestBuilders.get(SUCCESS_SHARES,24574)
                        .header("authorization", customerToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(788)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].share.id", is(24574)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].share.customer.id", is(13960)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].share.customer.userName", is("78677")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].gmtCreate", is("2021-12-21T15:15:02")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }


    @Test
    public void AdminRetrieveSuccessShares() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Mockito.when(orderItemDao.getOrderItemById(Mockito.any())).thenReturn(new InternalReturnObject<>(new OrderItem()));
        Mockito.when(productDao.getProductById(Mockito.any())).thenReturn(new InternalReturnObject<>(new Product()));
        Product product = new Product(3274L,"测试商品",114514L,123,null);
        Mockito.when(productDao.getSimpleProductById(Mockito.any())).thenReturn(new InternalReturnObject<>(product));
        Mockito.when(couponActivityDao.getCouponActivityByProductId(Mockito.anyLong())).thenReturn(new InternalReturnObject<>());
        Mockito.when(onsaleDao.getOnsaleById(Mockito.any())).thenReturn(new InternalReturnObject<>(new Onsale()));

        this.mockMvc.perform(MockMvcRequestBuilders.get(ONSALES_SUCCESSSHARES,2,33)
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("page", "1")
                .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(777)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].share.id", is(24577)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].share.customer.id", is(13276)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].share.customer.userName", is("561763")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].gmtCreate", is("2021-12-21T15:15:02")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

}
