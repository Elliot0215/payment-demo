package com.eryue.paymentdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eryue.paymentdemo.entity.PaymentInfo;
import com.eryue.paymentdemo.mapper.PaymentInfoMapper;
import com.eryue.paymentdemo.service.PaymentInfoService;
import org.springframework.stereotype.Service;

/**
 * @author 二月
 */
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {

}
