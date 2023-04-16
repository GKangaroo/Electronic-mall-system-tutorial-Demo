package cn.edu.xmu.oomall.alipay.controller;

import cn.edu.xmu.oomall.alipay.AliPayApplication;
import cn.edu.xmu.oomall.alipay.microservice.PaymentFeightService;
import cn.edu.xmu.oomall.alipay.util.NotifyReturnObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional      //防止脏数据
@SpringBootTest(classes = AliPayApplication.class)
@AutoConfigureMockMvc      //自动初始化MockMvc
class AlipayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "cn.edu.xmu.oomall.alipay.microservice.PaymentFeightService")
    private PaymentFeightService paymentFeightService;


    @Test
    void pay() throws Exception{

        //成功情况
        for (int i = 0; i < 8; i++) {
            String biz_content="{\"out_trade_no\":\"888888"+Integer.toString(i)+"\",\"total_amount\":100}";
            Mockito.when(paymentFeightService.notify(Mockito.any())).thenReturn(new NotifyReturnObject("SUCCESS","成功"));
            String responseString = this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/alipay/gateway.do")
                    .queryParam("method","alipay.trade.wap.pay")
                    .queryParam("biz_content",biz_content)
                    .contentType("application/json;charset=UTF-8"))
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);;
            String expectedString="{\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\",\"alipay_trade_wap_pay_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"out_trade_no\":\"888888"+Integer.toString(i)+"\",\"total_amount\":100,\"seller_id\":\"2088111111116894\",\"merchant_order_no\":\"20161008001\",\"trade_no\":\"2013112011001004330000121536\"}}";
            JSONAssert.assertEquals(expectedString,responseString,true);
        }


        //交易存在，且交易关闭
        String biz_content2="{\"out_trade_no\":\"1\",\"total_amount\":100}";
        Mockito.when(paymentFeightService.notify(Mockito.any())).thenReturn(new NotifyReturnObject("SUCCESS","成功"));
        String responseString2 = this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/alipay/gateway.do")
                .queryParam("method","alipay.trade.wap.pay")
                .queryParam("biz_content",biz_content2)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);;
        String expectedString2="{\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\",\"alipay_trade_wap_pay_response\":{\"code\":\"40004\",\"msg\":\"Business Failed\",\"sub_code\":\"ACQ.TRADE_HAS_CLOSE\",\"sub_msg\":\"交易已关闭\"}}";
        JSONAssert.assertEquals(expectedString2,responseString2,true);

        //交易存在，且交易已支付
        String biz_content3="{\"out_trade_no\":\"2\",\"total_amount\":100}";
        Mockito.when(paymentFeightService.notify(Mockito.any())).thenReturn(new NotifyReturnObject("SUCCESS","成功"));
        String responseString3 = this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/alipay/gateway.do")
                .queryParam("method","alipay.trade.wap.pay")
                .queryParam("biz_content",biz_content3)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);;
        String expectedString3="{\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\",\"alipay_trade_wap_pay_response\":{\"code\":\"40004\",\"msg\":\"Business Failed\",\"sub_code\":\"ACQ.TRADE_HAS_SUCCESS\",\"sub_msg\":\"交易已被支付\"}}";
        JSONAssert.assertEquals(expectedString3,responseString3,true);
    }

    @Test
    void payQuery() throws Exception {
        //交易不存在
        String biz_content1="{\"out_trade_no\":\"77887788\"}";
        Mockito.when(paymentFeightService.notify(Mockito.any())).thenReturn(new NotifyReturnObject("SUCCESS","成功"));
        String responseString1 = this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/alipay/gateway.do")
                .queryParam("method","alipay.trade.query")
                .queryParam("biz_content",biz_content1)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);;
        String expectedString1="{\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\",\"alipay_trade_query_response\":{\"code\":\"40004\",\"msg\":\"Business Failed\",\"sub_code\":\"ACQ.TRADE_NOT_EXIST\",\"sub_msg\":\"交易不存在\"}}";
        JSONAssert.assertEquals(expectedString1,responseString1,true);
        //交易存在
        String biz_content2="{\"out_trade_no\":\"1\"}";
        Mockito.when(paymentFeightService.notify(Mockito.any())).thenReturn(new NotifyReturnObject("SUCCESS","成功"));
        String responseString2 = this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/alipay/gateway.do")
                .queryParam("method","alipay.trade.query")
                .queryParam("biz_content",biz_content2)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);;
        String expectedString2="{\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\",\"alipay_trade_query_response\":{\"out_trade_no\":\"1\",\"send_pay_date\":\"2021-12-01 10:16:46\",\"trade_status\":\"TRADE_CLOSED\",\"total_amount\":100,\"buyer_pay_amount\":99,\"buyer_logon_id\":\"1595620\",\"trade_no\":\"2013112011001004330000121536\",\"credit_pay_mode\":\"creditAdvanceV2\",\"credit_biz_order_id\":\"ZMCB99202103310000450000041833\"}}";
        JSONAssert.assertEquals(expectedString2,responseString2,true);

    }


    @Test
    void close() throws Exception {
        //此状态为支付失败的状态，可以关闭
        String biz_content1="{\"out_trade_no\":\"3\"}";
        Mockito.when(paymentFeightService.notify(Mockito.any())).thenReturn(new NotifyReturnObject("SUCCESS","成功"));
        String responseString1 = this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/alipay/gateway.do")
                .queryParam("method","alipay.trade.close")
                .queryParam("biz_content",biz_content1)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);;
        String expectedString1="{\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\",\"alipay_trade_close_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"out_trade_no\":\"3\",\"trade_no\":\"2013112011001004330000121536\"}}";
        JSONAssert.assertEquals(expectedString1,responseString1,true);
        //此状态不为支付失败的状态，不可以关闭

        String biz_content2="{\"out_trade_no\":\"1\"}";
        Mockito.when(paymentFeightService.notify(Mockito.any())).thenReturn(new NotifyReturnObject("SUCCESS","成功"));
        String responseString2 = this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/alipay/gateway.do")
                .queryParam("method","alipay.trade.close")
                .queryParam("biz_content",biz_content2)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);;
        String expectedString2="{\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\",\"alipay_trade_close_response\":{\"code\":\"40004\",\"msg\":\"Business Failed\",\"sub_code\":\"ACQ.REASON_TRADE_STATUS_INVALID\",\"sub_msg\":\"交易状态不合法\"}}";
        JSONAssert.assertEquals(expectedString2,responseString2,true);
        //此单号不存在不能关
        String biz_content3="{\"out_trade_no\":\"77887788\"}";
        Mockito.when(paymentFeightService.notify(Mockito.any())).thenReturn(new NotifyReturnObject("SUCCESS","成功"));
        String responseString3 = this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/alipay/gateway.do")
                .queryParam("method","alipay.trade.close")
                .queryParam("biz_content",biz_content3)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);;
        String expectedString3="{\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\",\"alipay_trade_close_response\":{\"code\":\"40004\",\"msg\":\"Business Failed\",\"sub_code\":\"ACQ.TRADE_NOT_EXIST\",\"sub_msg\":\"交易不存在\"}}";
        JSONAssert.assertEquals(expectedString3,responseString3,true);
    }

    @Test
    void refund() throws Exception {
        //成功情况
        for (int i = 0; i < 8; i++) {
            String biz_content="{\"out_trade_no\":\"2\",\"out_request_no\":\"2"+i+"\",\"refund_amount\":1}";
            Mockito.when(paymentFeightService.notify(Mockito.any())).thenReturn(new NotifyReturnObject("SUCCESS","成功"));
            String responseString = this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/alipay/gateway.do")
                    .queryParam("method","alipay.trade.refund")
                    .queryParam("biz_content",biz_content)
                    .contentType("application/json;charset=UTF-8"))
                    .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
            //退款总额
            int refund_fee=90+i+1;
            String expectedString="{\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\",\"alipay_trade_refund_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"out_trade_no\":\"2\",\"refund_fee\":"+refund_fee+",\"trade_no\":\"2013112011001004330000121536\",\"buyer_logon_id\":\"1595620\",\"fund_change\":\"Y\",\"buyer_user_id\":\"2088101117955611\"}}";
            JSONAssert.assertEquals(expectedString,responseString,true);
        }
        //订单不存在
        String biz_content2="{\"out_trade_no\":\"778887\",\"out_request_no\":\"7788\",\"refund_amount\":1}";
        Mockito.when(paymentFeightService.notify(Mockito.any())).thenReturn(new NotifyReturnObject("SUCCESS","成功"));
        String responseString2 = this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/alipay/gateway.do")
                .queryParam("method","alipay.trade.refund")
                .queryParam("biz_content",biz_content2)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expectedString2="{\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\",\"alipay_trade_refund_response\":{\"code\":\"40004\",\"msg\":\"Business Failed\",\"sub_code\":\"ACQ.TRADE_NOT_EXIST\",\"sub_msg\":\"交易不存在\"}}";
        JSONAssert.assertEquals(expectedString2,responseString2,true);
        //订单状态不为成功，不允许退款
        String biz_content3="{\"out_trade_no\":\"1\",\"out_request_no\":\"7788\",\"refund_amount\":1}";
        Mockito.when(paymentFeightService.notify(Mockito.any())).thenReturn(new NotifyReturnObject("SUCCESS","成功"));
        String responseString3 = this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/alipay/gateway.do")
                .queryParam("method","alipay.trade.refund")
                .queryParam("biz_content",biz_content3)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expectedString3="{\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\",\"alipay_trade_refund_response\":{\"code\":\"40004\",\"msg\":\"Business Failed\",\"sub_code\":\"ACQ.TRADE_NOT_ALLOW_REFUND\",\"sub_msg\":\"当前交易不允许退款\"}}";
        JSONAssert.assertEquals(expectedString3,responseString3,true);
        //退款金额超限
        String biz_content4="{\"out_trade_no\":\"2\",\"out_request_no\":\"7788\",\"refund_amount\":100}";
        Mockito.when(paymentFeightService.notify(Mockito.any())).thenReturn(new NotifyReturnObject("SUCCESS","成功"));
        String responseString4 = this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/alipay/gateway.do")
                .queryParam("method","alipay.trade.refund")
                .queryParam("biz_content",biz_content4)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expectedString4="{\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\",\"alipay_trade_refund_response\":{\"code\":\"40004\",\"msg\":\"Business Failed\",\"sub_code\":\"ACQ.REFUND_AMT_NOT_EQUAL_TOTAL\",\"sub_msg\":\"退款金额超限\"}}";
        JSONAssert.assertEquals(expectedString4,responseString4,true);
    }

    @Test
    void refundQuery() throws Exception{
        //查询失败，该退款单不存在
        String biz_content1="{\"out_trade_no\":\"2\",\"out_request_no\":\"7788\"}";
        Mockito.when(paymentFeightService.notify(Mockito.any())).thenReturn(new NotifyReturnObject("SUCCESS","成功"));
        String responseString1 = this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/alipay/gateway.do")
                .queryParam("method","alipay.trade.refund.query")
                .queryParam("biz_content",biz_content1)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expectedResponse1="{\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\",\"alipay_trade_fastpay_refund_query_response\":{\"code\":\"40004\",\"msg\":\"Business Failed\",\"sub_code\":\"ACQ.TRADE_NOT_EXIST\",\"sub_msg\":\"交易不存在\"}}";
        JSONAssert.assertEquals(expectedResponse1,responseString1,true);

        //查询成功
        String biz_content2="{\"out_trade_no\":\"2\",\"out_request_no\":\"1\"}";
        Mockito.when(paymentFeightService.notify(Mockito.any())).thenReturn(new NotifyReturnObject("SUCCESS","成功"));
        String responseString2 = this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/alipay/gateway.do")
                .queryParam("method","alipay.trade.refund.query")
                .queryParam("biz_content",biz_content2)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expectedResponse2="{\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\",\"alipay_trade_fastpay_refund_query_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"out_trade_no\":\"2\",\"out_request_no\":\"1\",\"total_amount\":100,\"refund_amount\":90,\"refund_status\":\"REFUND_SUCCESS\",\"gmt_refund_pay\":\"2021-12-01 10:56:17\",\"trade_no\":\"2013112011001004330000121536\"}}";
        JSONAssert.assertEquals(expectedResponse2,responseString2,true);
    }

    @Test
    void downloadUrlQuery() throws Exception {
        String biz_content1="{}";
        Mockito.when(paymentFeightService.notify(Mockito.any())).thenReturn(new NotifyReturnObject("SUCCESS","成功"));
        String responseString1 = this.mockMvc.perform(MockMvcRequestBuilders.post("/internal/alipay/gateway.do")
                .queryParam("method","alipay.data.dataservice.bill.downloadurl.query")
                .queryParam("biz_content",biz_content1)
                .contentType("application/json;charset=UTF-8"))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
        String expectedResponse1="{\"sign\":\"ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE\",\"alipay_data_dataservice_bill_downloadurl_query_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"bill_download_url\":\"https://dwbillcenter.alipay.com/downloadBillFile\"}}";
        JSONAssert.assertEquals(expectedResponse1,responseString1,true);
    }
}