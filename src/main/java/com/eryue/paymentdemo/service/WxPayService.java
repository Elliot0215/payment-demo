package com.eryue.paymentdemo.service;

import java.io.IOException;
import java.util.Map;

/**
 * @author 二月
 */
public interface WxPayService {
    Map<String, Object> nativePay(Long productId) throws IOException, Exception;
}
