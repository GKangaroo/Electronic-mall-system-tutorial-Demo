package cn.edu.xmu.oomall.aftersale.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.aftersale.AftersaleApplication;
import cn.edu.xmu.oomall.aftersale.dao.ArbitrationDao;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

@SpringBootTest(classes = AftersaleApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ArbitrationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    RedisUtil redisUtil;

    static String adminToken;
    static String adminToken2;
    static String shopToken;
    static String userToken;

    private static final String arbitrationsStates = "/arbitrations/states";
    private static final String aftersalesStates = "/aftersales/states";
    private static final String aftersalesIdArbitrations = "/aftersales/{id}/arbitrations";
    private static final String shopsShopIdArbitrations = "/shops/{shopId}/arbitrations";
    private static final String shopsShopIdArbitrationsIdAccept = "/shops/{shopId}/arbitrations/{id}/accept";
    private static final String shopsShopIdArbitrationsSelf = "/shops/{shopId}/arbitrations/self";
    private static final String shopsShopIdArbitrationsId = "/shops/{shopId}/arbitrations/{id}";
    private static final String shopsShopIdArbitrationsIdDelete = "/shops/{shopId}/arbitrations/{id}";
    private static final String shopsShopIdArbitrationsIdClose = "/shops/{shopId}/arbitrations/{id}/close";

    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "admin13088", 0L, 1, 3600);
        adminToken2 = jwtHelper.createToken(2L, "admin114514", 0L, 1, 3600);
        shopToken = jwtHelper.createToken(15L, "shop1", 1L, 1, 3600);
        userToken = jwtHelper.createToken(15L, "user1", 1L, 0, 3600);
    }

    @Test
    public void getAllArbitrationStatesTest() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(arbitrationsStates)
                        .header("authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code==0)].name", hasItem("申请")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code==1)].name", hasItem("受理")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code==2)].name", hasItem("撤销")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code==3)].name", hasItem("结案")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void getAllAftersalesStatesTest() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(aftersalesStates)
                        .header("authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code==0)].name", hasItem("申请")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code==1)].name", hasItem("处理中")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code==2)].name", hasItem("分派服务商")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code==3)].name", hasItem("待退款")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code==4)].name", hasItem("发出换货订单")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code==6)].name", hasItem("结束")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[?(@.code==7)].name", hasItem("取消")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void createArbitrationTest1() throws Exception {
        String body = "{\"type\":1,\"quantity\":2,\"reason\":\"Reason\",\"regionId\":1,\"detail\":\"Detail\",\"consignee\":\"Consignee\",\"mobile\":\"110\"}";
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.post(aftersalesIdArbitrations, 1)
                        .header("Authorization", adminToken)
                        .content(body.getBytes("utf-8"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    public void createArbitrationTest2() throws Exception {
        String body = "{\"type\":1,\"quantity\":2,\"reason\":\"Reason\",\"regionId\":1,\"detail\":\"Detail\",\"consignee\":\"Consignee\",\"mobile\":\"110\"}";
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.post(aftersalesIdArbitrations, 1)
                        .header("Authorization", adminToken)
                        .content(body.getBytes("utf-8"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void findUnacceptedArbitrationsTest() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(shopsShopIdArbitrations, 1)
                        .header("Authorization", adminToken)
                        .param("page", "1")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void acceptArbitrationsTest1() throws Exception {
        String body = "{\"arbitrationId\":1,\"arbitrationName\":\"Jack\"}";
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.put(shopsShopIdArbitrationsIdAccept, 1, 1)
                        .header("Authorization", adminToken)
                        .content(body.getBytes("utf-8"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void acceptArbitrationsTest2() throws Exception {
        String body = "{\"arbitrationId\":2,\"arbitrationName\":\"Jack\"}";
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.put(shopsShopIdArbitrationsIdAccept, 1, 3)
                        .header("Authorization", adminToken)
                        .content(body.getBytes("utf-8"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.AUTH_NO_RIGHT.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void acceptArbitrationsTest3() throws Exception {
        String body = "{\"arbitrationId\":1,\"arbitrationName\":\"Jack\"}";
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.put(shopsShopIdArbitrationsIdAccept, 1, 1000)
                        .header("Authorization", adminToken)
                        .content(body.getBytes("utf-8"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void findArbitrationTest() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(shopsShopIdArbitrationsSelf, 1)
                        .header("Authorization", adminToken)
                        .param("page", "1")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void findArbitrationByIdTest1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(shopsShopIdArbitrationsId, 1, 1)
                        .header("Authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id", is(1)))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void findArbitrationByIdTest2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(shopsShopIdArbitrationsId, 1, 2)
                        .header("Authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").isNotEmpty())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void findArbitrationByIdTest3() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(shopsShopIdArbitrationsId, 1, 1000)
                        .header("Authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void deleteArbitrationByIdTest1() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(shopsShopIdArbitrationsIdDelete, 1, 2)
                        .header("Authorization", userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.AUTH_NO_RIGHT.getErrNo())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errmsg", is("无权限")))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void deleteArbitrationByIdTest2() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(shopsShopIdArbitrationsIdDelete, 1, 2)
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void deleteArbitrationByIdTest3() throws Exception {
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(shopsShopIdArbitrationsIdDelete, 1, 1000)
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void closeArbitrationsTest1() throws Exception {
        String body = "{\"result\":\"no result\"}";
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.put(shopsShopIdArbitrationsIdClose, 1, 1)
                        .header("Authorization", adminToken)
                        .content(body.getBytes("utf-8"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void closeArbitrationsTest2() throws Exception {
        String body = "{\"result\":\"no result\"}";
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.put(shopsShopIdArbitrationsIdClose, 1, 1000)
                        .header("Authorization", adminToken)
                        .content(body.getBytes("utf-8"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.RESOURCE_ID_NOTEXIST.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
    @Test
    public void closeArbitrationsTest3() throws Exception {
        String body = "{\"result\":\"no result\"}";
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.put(shopsShopIdArbitrationsIdClose, 1, 1)
                        .header("Authorization", adminToken2)
                        .content(body.getBytes("utf-8"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().is(403))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.AUTH_NO_RIGHT.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
