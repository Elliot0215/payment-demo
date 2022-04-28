package com.eryue.paymentdemo.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 二月
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.eryue.paymentdemo.mapper")
public class MybatisPlusConfig {
}
