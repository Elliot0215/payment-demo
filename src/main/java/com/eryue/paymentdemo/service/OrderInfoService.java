package com.eryue.paymentdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eryue.paymentdemo.entity.OrderInfo;

import java.util.List;

/**
 * @author 二月
 */
public interface OrderInfoService extends IService<OrderInfo> {
    OrderInfo createOrderByProductId(Long productId);
    void saveCodeUrl(String orderNo, String codeUrl);
    List<OrderInfo> getOrderListByCreateTime();
}
