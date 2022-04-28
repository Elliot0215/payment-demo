package com.eryue.paymentdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.eryue.paymentdemo.entity.Product;
import com.eryue.paymentdemo.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductMapper mockProductMapper;

    @InjectMocks
    private ProductServiceImpl productServiceImplUnderTest;

    @Test
    void testGetProductList() {
        // Setup
        final Product product = new Product();
        product.setTitle("title");
        product.setPrice(0);
        final List<Product> expectedResult = Arrays.asList(product);

        // Configure ProductMapper.selectList(...).
        final Product product1 = new Product();
        product1.setTitle("title");
        product1.setPrice(0);
        final List<Product> products = Arrays.asList(product1);
        when(mockProductMapper.selectList(any(Wrapper.class))).thenReturn(products);

        // Run the test
        final List<Product> result = productServiceImplUnderTest.getProductList();

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetProductList_ProductMapperReturnsNoItems() {
        // Setup
        when(mockProductMapper.selectList(any(Wrapper.class))).thenReturn(Collections.emptyList());

        // Run the test
        final List<Product> result = productServiceImplUnderTest.getProductList();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }
}
