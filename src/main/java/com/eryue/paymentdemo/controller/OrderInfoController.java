package com.eryue.paymentdemo.controller;

import com.eryue.paymentdemo.entity.OrderInfo;
import com.eryue.paymentdemo.enums.OrderStatus;
import com.eryue.paymentdemo.service.OrderInfoService;
import com.eryue.paymentdemo.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("查询本地订单状态")
    @GetMapping("/query-order-status/{orderNo}")
    public Result queryOrderStatus(@PathVariable String orderNo){
        String orderStatus = orderInfoService.getOrderStatus(orderNo);
        if (OrderStatus.SUCCESS.getType().equals(orderStatus)){
            return Result.ok();
        }
        return Result.ok().setCode(101).setMessage("支付中...");
    }

}
