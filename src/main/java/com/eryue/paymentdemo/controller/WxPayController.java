package com.eryue.paymentdemo.controller;


import com.eryue.paymentdemo.service.WxPayService;
import com.eryue.paymentdemo.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 二月
 */
@RestController
@CrossOrigin
@Api(tags = "微信支付")
@RequestMapping("/api/wxpay")
@Slf4j
public class WxPayController {

    @Resource
    private WxPayService payService;

    @ApiOperation("生成支付二维码")
    @PostMapping("/native/{productId}")
    public Result nativePay(@PathVariable Long productId) throws Exception {
        log.info("发起支付请求，入参productId:{}",productId);
        Map<String,Object> resultMap = payService.nativePay(productId);
        return Result.ok().setData(resultMap);
    }
}
