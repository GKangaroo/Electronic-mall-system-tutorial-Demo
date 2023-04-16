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


import static org.hamcrest.CoreMatchers.is;

@SpringBootTest(classes = CustomerApplication.class)
@AutoConfigureMockMvc
@Transactional
public class CouponControllerTest {

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

    private static final String COUPONS = "/coupons";
    private static final String COUPON_ACTIVITIES = "/couponActivities/{id}/coupons";

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "admin", 0L, 1, 3600);
        shopToken = jwtHelper.createToken(15L, "shop1", 1L, 1, 3600);
        customerToken = jwtHelper.createToken(1001L,"696371",1L,1,3600);
    }

    @Test
    public void retrieveCoupons() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(COUPONS)
                        .header("authorization", customerToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("actId", "3")
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.page", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pageSize", is(10)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void receiveCoupons1() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.post(COUPON_ACTIVITIES,1)
                        .header("authorization", customerToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0]", is("C001")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

//    @Test
//    public void receiveCoupons2() throws Exception{
//        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
//        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
//        this.mockMvc.perform(MockMvcRequestBuilders.post(COUPON_ACTIVITIES,1)
//                        .header("authorization", customerToken)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(MockMvcResultMatchers.status().isForbidden())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.COUPON_NOTBEGIN.getErrNo())))
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//    }

//    @Test
//    public void receiveCoupons3() throws Exception{
//        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
//        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
//        this.mockMvc.perform(MockMvcRequestBuilders.post(COUPON_ACTIVITIES,1)
//                        .header("authorization", customerToken)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(MockMvcResultMatchers.status().isForbidden())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.COUPON_FINISH.getErrNo())))
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//    }
//
//    @Test
//    public void receiveCoupons4() throws Exception{
//        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
//        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
//        this.mockMvc.perform(MockMvcRequestBuilders.post(COUPON_ACTIVITIES,2)
//                        .header("authorization", customerToken)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(MockMvcResultMatchers.status().isForbidden())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.COUPON_END.getErrNo())))
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//    }
//
//    @Test
//    public void receiveCoupons5() throws Exception{
//        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
//        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
//        this.mockMvc.perform(MockMvcRequestBuilders.post(COUPON_ACTIVITIES,1)
//                        .header("authorization", customerToken)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(MockMvcResultMatchers.status().isForbidden())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.COUPON_EXIST.getErrNo())))
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn().getResponse().getContentAsString();
//    }
//

}
