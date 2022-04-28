package com.eryue.paymentdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.eryue.paymentdemo.entity.Product;

import java.util.List;

/**
 * @author 二月
 */
public interface ProductService extends IService<Product> {
    List<Product> getProductList();

}
