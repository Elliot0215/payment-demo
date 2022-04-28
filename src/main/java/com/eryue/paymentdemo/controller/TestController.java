package com.eryue.paymentdemo.controller;

import com.eryue.paymentdemo.config.WxPayConfig;
import com.eryue.paymentdemo.entity.Product;
import com.eryue.paymentdemo.service.ProductService;
import com.eryue.paymentdemo.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 二月
 */
@RestController
@RequestMapping("/api/product")
@CrossOrigin
@Api(tags = "商品管理")
public class TestController {
    @Resource
    private ProductService productService;

    @ApiOperation("test")
    @GetMapping("/test")
    public Result test(){
        return Result.ok().data("nowTime",new Date());
    }

    @ApiOperation("商品列表")
    @GetMapping("/list")
    public Result list(){
        List<Product> list = productService.list();
        return Result.ok().data("productList",list);
    }
}
