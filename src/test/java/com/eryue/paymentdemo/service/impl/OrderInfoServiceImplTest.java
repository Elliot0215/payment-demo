package com.eryue.paymentdemo.service.impl;

import com.eryue.paymentdemo.entity.OrderInfo;
import com.eryue.paymentdemo.entity.Product;
import com.eryue.paymentdemo.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderInfoServiceImplTest {

    @Autowired
    private ProductMapper mockProductMapper;

    @Autowired
    private OrderInfoServiceImpl orderInfoServiceImplUnderTest;

    @Test
    void testCreateOrderByProductId() {
        // Setup
        final OrderInfo expectedResult = new OrderInfo();
        expectedResult.setTitle("title");
        expectedResult.setOrderNo("orderNo");
        expectedResult.setUserId(0L);
        expectedResult.setProductId(1L);
        expectedResult.setTotalFee(0);
        expectedResult.setCodeUrl("codeUrl");
        expectedResult.setOrderStatus("type");

        // Configure ProductMapper.selectById(...).
        final Product product = new Product();
        product.setTitle("title");
        product.setPrice(0);
        when(mockProductMapper.selectById(1)).thenReturn(product);

        // Run the test
        final OrderInfo result = orderInfoServiceImplUnderTest.createOrderByProductId(1L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testSaveCodeUrl() {
        // Setup
        // Run the test
        orderInfoServiceImplUnderTest.saveCodeUrl("orderNo", "codeUrl");

        // Verify the results
    }
}
