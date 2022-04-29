package com.eryue.paymentdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.eryue.paymentdemo.config.WxPayConfig;
import com.eryue.paymentdemo.entity.OrderInfo;
import com.eryue.paymentdemo.enums.OrderStatus;
import com.eryue.paymentdemo.enums.wxpay.WxApiType;
import com.eryue.paymentdemo.enums.wxpay.WxNotifyType;
import com.eryue.paymentdemo.service.OrderInfoService;
import com.eryue.paymentdemo.service.PaymentInfoService;
import com.eryue.paymentdemo.service.WxPayService;
import com.eryue.paymentdemo.util.OrderNoUtils;
import com.google.gson.Gson;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 二月
 */
@Service
@Slf4j
public class WxPayServiceImpl implements WxPayService {
    @Autowired
    private WxPayConfig payConfig;

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Qualifier("wxPayClient")
    @Resource
    private CloseableHttpClient httpClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> nativePay(Long productId) throws Exception {
        log.info("生成订单");
        //创建订单
        OrderInfo orderInfo = orderInfoService.createOrderByProductId(productId);

        //封装请求
        HttpPost httpPost = new HttpPost(payConfig.getDomain().concat(WxApiType.NATIVE_PAY.getType()));

        // 请求body参数
        Gson gson = new Gson();
        Map paramsMap = new HashMap();
        paramsMap.put("appid", payConfig.getAppid());
        paramsMap.put("mchid", payConfig.getMchId());
        paramsMap.put("description", orderInfo.getTitle());
        paramsMap.put("out_trade_no", orderInfo.getOrderNo());
        paramsMap.put("notify_url", payConfig.getNotifyDomain().concat(WxNotifyType.NATIVE_NOTIFY.getType()));
        Map amountMap = new HashMap();
        amountMap.put("total", orderInfo.getTotalFee());
        amountMap.put("currency", "CNY");
        paramsMap.put("amount", amountMap);

        //转json字符串
        String jsonParams = gson.toJson(paramsMap);
        log.info("请求参数：" + jsonParams);
        StringEntity entity = new StringEntity(jsonParams, "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        CloseableHttpResponse execute = httpClient.execute(httpPost);

        try {
            String bodyAsString = EntityUtils.toString(execute.getEntity());
            int statusCode = execute.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info("成功, 返回结果 = " + bodyAsString);
            } else if (statusCode == 204) {
                log.info("成功");
            } else {
                log.info("Native下单失败,响应码 = " + statusCode + ",返回结果 = " + bodyAsString);
                throw new IOException("request failed");
            }

            Map<String, String> resultMap = gson.fromJson(bodyAsString, HashMap.class);

            String codeUrl = resultMap.get("code_url");
            orderInfoService.saveCodeUrl(orderInfo.getOrderNo(), codeUrl);
            Map<String, Object> map = new HashMap<>();
            map.put("codeUrl", codeUrl);
            map.put("orderNo", orderInfo.getOrderNo());
            return map;
        } finally {
            execute.close();
        }
    }

    @Override
    public void processOrder(Map<String, Object> bodyMap) throws GeneralSecurityException {
        log.info("处理订单");
        String plainText = decryptFromResource(bodyMap);

        // 转换明文
        Gson gson = new Gson();
        Map<String, Object> plainTextMap = gson.fromJson(plainText, HashMap.class);
        String orderNo = (String) plainTextMap.get("out_trade_no");
        // 更新订单状态
        orderInfoService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);
        // 记录支付日志
        paymentInfoService.createPaymentInfo(plainText);
    }

    /**
     * 对称解密
     *
     * @param bodyMap
     * @return
     */
    private String decryptFromResource(Map<String, Object> bodyMap) throws GeneralSecurityException {
        log.info("密文解密");
        Map<String, String> resourceMap = (Map) bodyMap.get("resource");
        String ciphertext = resourceMap.get("ciphertext");
        String nonce = resourceMap.get("nonce");
        String associatedData = resourceMap.get("associated_data");

        log.info("密文 ===> {}", ciphertext);
        AesUtil aesUtil = new AesUtil(payConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        String plainText = aesUtil.decryptToString(associatedData.getBytes(StandardCharsets.UTF_8), nonce.getBytes(StandardCharsets.UTF_8), ciphertext);
        log.info("明文 ===> {}", plainText);
        return plainText;
    }
}
