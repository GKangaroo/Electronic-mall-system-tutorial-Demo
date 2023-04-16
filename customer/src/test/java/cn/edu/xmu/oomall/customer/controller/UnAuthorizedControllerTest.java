package cn.edu.xmu.oomall.customer.controller;

import cn.edu.xmu.javaee.core.model.InternalReturnObject;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.customer.CustomerApplication;
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

import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = CustomerApplication.class)
@AutoConfigureMockMvc
@Transactional
public class UnAuthorizedControllerTest {

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

    static  String creatorToken;

    private static final String CUSTOMERS = "/customers";
    private static final String CUSTOMERS_PASSWORD = "/customers/password";
    private static final String CUSTOMERS_PASSWORD_RESET = "/customers/password/reset";
    private static final String CUSTOMERS_LOGIN = "/customers/login";
    private static final String CUSTOMERS_LOGOUT = "/customers/logout";
    private static final String CARTS = "/carts";
    private static final String CART = "/carts/{id}";


    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "admin", 0L, 1, 3600);
        shopToken = jwtHelper.createToken(15L, "shop1", 1L, 1, 3600);
        customerToken = jwtHelper.createToken(1L,"699275",1L,1,3600);
        creatorToken = jwtHelper.createToken(12191L,"180092",1L,1,3600);
    }

    @Test
    public void createCustomer1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"userName\":\"699275\",\"password\":\"123456\",\"name\":\"test\",\"mobile\":\"12345678900\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(CUSTOMERS)
                        .content(body.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CUSTOMER_NAMEEXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void createCustomer2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"userName\":\"699212175\",\"password\":\"123456\",\"name\":\"赵永波\",\"mobile\":\"13159235540\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(CUSTOMERS)
                        .content(body.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CUSTOMER_MOBILEEXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void createCustomer3() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"userName\":\"69239275\",\"password\":\"123456\",\"name\":\"赵永博\",\"mobile\":\"131535540\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(CUSTOMERS)
                        .content(body.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findCustomerById() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(CUSTOMERS)
                        .header("authorization", customerToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userName", is("699275")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name", is("赵永波")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.creator.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.creator.userName", is("admin")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void updateCustomerById1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"name\":\"赵永波\",\"mobile\":\"13159235541\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CUSTOMERS)
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
    public void updateCustomerById2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"name\":\"赵永波\",\"mobile\":\"13159235540\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CUSTOMERS)
                        .header("authorization", customerToken)
                        .content(body.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(0)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void updatePassword1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"captcha\":\"1234\",\"newPassword\":\"234567\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CUSTOMERS_PASSWORD)
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
    public void updatePassword2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"captcha\":\"1234\",\"newPassword\":\"123456\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CUSTOMERS_PASSWORD)
                        .header("authorization", customerToken)
                        .content(body.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CUSTOMER_PASSWORDSAME.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findCustomerById1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"userName\":\"699275\",\"mobile\":\"13159235540\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CUSTOMERS_PASSWORD_RESET)
                        .header("authorization", customerToken)
                        .content(body.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void findCustomerById2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"userName\":\"699275\",\"mobile\":\"13159235541\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CUSTOMERS_PASSWORD_RESET)
                        .header("authorization", customerToken)
                        .content(body.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CUSTOMER_MOBILEDIFF.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void customerLogin1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"userName\":\"699275\",\"password\":\"123456\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(CUSTOMERS_LOGIN)
                        .content(body.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void customerLogin2() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"userName\":\"699276\",\"password\":\"123456\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(CUSTOMERS_LOGIN)
                        .content(body.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CUSTOMER_INVALID_ACCOUNT.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void customerLogin3() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"userName\":\"699275\",\"password\":\"234567\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(CUSTOMERS_LOGIN)
                        .content(body.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CUSTOMER_INVALID_ACCOUNT.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

//    @Test
//    public void customerLogin4() throws Exception{
//        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
//        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
//        String body = "{\"userName\":\"699275\",\"password\":\"123456\"}";
//        this.mockMvc.perform(MockMvcRequestBuilders.post(CUSTOMERS_LOGIN)
//                        .content(body.getBytes(StandardCharsets.UTF_8))
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(MockMvcResultMatchers.status().isForbidden())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CUSTOMER_FORBIDDEN.getErrNo())))
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//    }

    @Test
    public void customerLogout() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(CUSTOMERS_LOGOUT)
                        .header("authorization", customerToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void retrieveCarts() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Product product = new Product(3274L,"测试商品",114514L,123,null);
        Mockito.when(productDao.getSimpleProductById(Mockito.any())).thenReturn(new InternalReturnObject<>(product));
        Mockito.when(couponActivityDao.getCouponActivityByProductId(Mockito.anyLong())).thenReturn(new InternalReturnObject<>());

        this.mockMvc.perform(MockMvcRequestBuilders.get(CARTS)
                        .header("authorization", creatorToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].id", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].product.id", is(3274)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].quantity", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].price", is(21947)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.list[0].gmtCreate", is("2021-12-21T15:31:12")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void createCarts() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        Product product = new Product(1L,"测试商品",114514L,123,null);
        Mockito.when(productDao.getSimpleProductById(Mockito.any())).thenReturn(new InternalReturnObject<>(product));
        Mockito.when(couponActivityDao.getCouponActivityByProductId(Mockito.anyLong())).thenReturn(new InternalReturnObject<>());
        String body = "{\"productId\":\"1\",\"quantity\":\"2\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(CARTS)
                        .header("authorization", creatorToken)
                        .content(body.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.product.id", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.quantity", is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.price", is(114514)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteCarts() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(CARTS)
                        .header("authorization", creatorToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void updateCart() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        String body = "{\"productId\":\"3274\",\"quantity\":\"2\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(CART,2)
                        .header("authorization", creatorToken)
                        .content(body.getBytes(StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteCart() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(CART,2)
                        .header("authorization", creatorToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }


}
