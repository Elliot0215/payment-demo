package com.eryue.paymentdemo.service.impl;

import com.eryue.paymentdemo.entity.Product;
import com.eryue.paymentdemo.mapper.ProductMapper;
import com.eryue.paymentdemo.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 二月
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> getProductList() {
        return productMapper.selectList(null);
    }
}
