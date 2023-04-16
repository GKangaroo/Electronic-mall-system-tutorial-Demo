package cn.edu.xmu.oomall.service.controller;

import cn.edu.xmu.javaee.core.model.ReturnNo;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.util.RedisUtil;
import cn.edu.xmu.oomall.service.ServiceApplication;
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

/**
 * @author：guiqingxin
 * @date：2022/12/20 10:30
 */
@SpringBootTest(classes = ServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ServiceControllerTest {
    private static final String SERVICE_STATE = "/services/states";
    private static final String SERVICE_SHOP = "/shops/{did}/services";
    private static final String SERVICE_ID = "/shops/{did}/services/{id}";
    private static final String SERVICE_DE = "/shops/{did}/services/{id}";
    private static final String SERVICE_RE = "/maintainers/{did}/services";
    private static final String SERVICE_FI = "/maintainers/{did}/services/{id}";
    private static final String SERVICE_AC = "/maintainers/{did}/services/{id}/accept";
    private static final String SERVICE_REF = "/maintainers/{did}/services/{id}/refuse";
    private static final String SERVICE_REC = "/maintainers/{did}/services/{id}/receive";
    private static final String SERVICE_FIN = "/maintainers/{did}/service/{id}/finish";
    private static final String SERVICE_CA =  "/maintainers/{did}/service/{id}/cancel";
    private static final String SERVICE_BUI =  "/internal/shops/{shopId}/orderitems/{id}/maintainers/{mid}";
    @Autowired
    MockMvc mockMvc;
    @MockBean
    RedisUtil redisUtil;
    static String adminToken;

    static String serviceToken;

    JwtHelper jwtHelper = new JwtHelper();
    @BeforeAll
    public static void setup() {
        JwtHelper jwtHelper = new JwtHelper();
        adminToken = jwtHelper.createToken(1L, "13088admin", 0L, 1, 3600);
        serviceToken = jwtHelper.createToken(15L, "shop1", 1L, 1, 3600);
    }

    @Test
    public void retrieveServiceStates1() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get(SERVICE_STATE).header("authorization", adminToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].code", is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name", is("新建")))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void retrieveServiceStates2() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get(SERVICE_STATE)
                .header("authorization", adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].code", is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name", is("已分派")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void retrieveAllServices() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.get(SERVICE_SHOP,1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void findServiceById() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(SERVICE_ID,1,1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void deleteServiceById() throws Exception{
        //Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        //Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.delete(SERVICE_DE,1,1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void retrieveMaintainerServices() throws Exception{
        //Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        //Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(SERVICE_RE,1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("status", String.valueOf((byte)1))
                        .param("page", "1")
                        .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void findMaintainerServiceById() throws Exception{
        Mockito.when(redisUtil.hasKey(Mockito.anyString())).thenReturn(false);
        Mockito.when(redisUtil.set(Mockito.anyString(), Mockito.any(), Mockito.anyLong())).thenReturn(true);
        this.mockMvc.perform(MockMvcRequestBuilders.get(SERVICE_FI,1,1)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void acceptService() throws Exception{
        String body = "{\"confirm\":true,\"maintainerName\":\"name\",\"maintainerMobile\":123}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(SERVICE_AC,4,4)
                .header("authorization", adminToken)
                .content(body.getBytes("utf-8"))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void refuseService() throws Exception{
        this.mockMvc.perform(MockMvcRequestBuilders.put(SERVICE_REF,4,4)
                        .header("authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void receiveService() throws Exception{
        String body = "{\"accepted\":true,\"result\":\"name\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(SERVICE_REC,6,6)
                        .header("authorization", adminToken)
                        .content(body.getBytes("utf-8"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void finishService() throws Exception{
        String body = "{\"result\":\"123123\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(SERVICE_FIN,7,7)
                        .header("authorization", adminToken)
                        .content(body.getBytes("utf-8"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void cancelService() throws Exception{
        String body = "{\"result\":\"name\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.put(SERVICE_CA,4,4)
                        .header("authorization", adminToken)
                        .content(body.getBytes("utf-8"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.OK.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void buildService() throws Exception{
        String body = "{\"type\":1,\"consignee\":{\"name\":\"John\",\"mobile\":\"123\",\"regionId\":1,\"address\":\"test\"}}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(SERVICE_BUI,9,9,9)
                        .header("authorization", adminToken)
                        .content(body.getBytes("utf-8"))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errno", is(ReturnNo.CREATED.getErrNo())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }
}
