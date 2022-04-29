package com.eryue.paymentdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eryue.paymentdemo.entity.PaymentInfo;
import com.eryue.paymentdemo.enums.PayType;
import com.eryue.paymentdemo.mapper.PaymentInfoMapper;
import com.eryue.paymentdemo.service.PaymentInfoService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 二月
 */
@Service
@Slf4j
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {

    /**
     * 记录支付日志
     *
     * @param plainText
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPaymentInfo(String plainText) {
        log.info("记录支付日志");

        Gson gson = new Gson();
        Map<String, Object> plainTextMap = gson.fromJson(plainText, HashMap.class);
        String orderNo = (String) plainTextMap.get("out_trade_no");
        String transactionId = (String) plainTextMap.get("transaction_id");
        String tradeType = (String) plainTextMap.get("trade_type");
        String tradeState = (String) plainTextMap.get("trade_state");
        Map<String, Object> amount = (Map) plainTextMap.get("amount");
        Integer payerTotal = ((Double) amount.get("payer_total")).intValue();
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderNo(orderNo);
        paymentInfo.setPaymentType(PayType.WXPAY.getType());
        paymentInfo.setTransactionId(transactionId);
        paymentInfo.setTradeType(tradeType);
        paymentInfo.setTradeState(tradeState);
        paymentInfo.setPayerTotal(payerTotal);
        paymentInfo.setContent(plainText);
        paymentInfo.setIsDeleted(0);
        baseMapper.insert(paymentInfo);
    }
}
