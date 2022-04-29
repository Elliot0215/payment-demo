package com.eryue.paymentdemo.controller;


import com.eryue.paymentdemo.service.WxPayService;
import com.eryue.paymentdemo.util.HttpUtils;
import com.eryue.paymentdemo.vo.Result;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 二月
 */
@RestController
@CrossOrigin
@Api(tags = "微信支付")
@RequestMapping("/api/wx-pay")
@Slf4j
public class WxPayController {

    @Resource
    private WxPayService payService;

    @ApiOperation("生成支付二维码")
    @PostMapping("/native/{productId}")
    public Result nativePay(@PathVariable Long productId) throws Exception {
        log.info("发起支付请求，入参productId:{}", productId);
        Map<String, Object> resultMap = payService.nativePay(productId);
        return Result.ok().setData(resultMap);
    }

    /**
     * 支付通知 * 微信支付通过支付通知接口将用户支付成功消息通知给商户
     */
    @ApiOperation("支付通知")
    @PostMapping("/native/notify")
    public String nativeNotify(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();

        String body = HttpUtils.readData(request);
        Map<String, Object> bodyMap = gson.fromJson(body, HashMap.class);
        log.info("支付通知的id ===> {}", bodyMap.get("id"));
        log.info("支付通知的完整数据 ===> {}", body);

        response.setStatus(200);
        map.put("code", "SUCCESS");
        map.put("message", "成功");
        return gson.toJson(map);
    }
}
