package com.eryue.paymentdemo.controller;

import com.eryue.paymentdemo.entity.OrderInfo;
import com.eryue.paymentdemo.service.OrderInfoService;
import com.eryue.paymentdemo.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 二月
 */
@RestController
@CrossOrigin
@Api(tags = "订单管理")
@RequestMapping("/api/orderInfo")
@Slf4j
public class OrderInfoController {
    @Autowired
    private OrderInfoService orderInfoService;

    @ApiOperation("订单列表")
    @GetMapping("/getOrderList")
    public Result getOrderList(){
        List<OrderInfo> orderListByCreateTime = orderInfoService.getOrderListByCreateTime();
        return Result.ok().data("orderList",orderListByCreateTime);
    }
}
