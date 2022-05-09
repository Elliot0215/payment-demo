package com.eryue.paymentdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eryue.paymentdemo.entity.OrderInfo;
import com.eryue.paymentdemo.entity.Product;
import com.eryue.paymentdemo.enums.OrderStatus;
import com.eryue.paymentdemo.enums.wxpay.WxApiType;
import com.eryue.paymentdemo.mapper.OrderInfoMapper;
import com.eryue.paymentdemo.mapper.ProductMapper;
import com.eryue.paymentdemo.service.OrderInfoService;
import com.eryue.paymentdemo.util.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author 二月
 */
@Service
@Slf4j
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional
    public OrderInfo createOrderByProductId(Long productId) throws Exception {
        //查询是否存在未支付的订单
        OrderInfo orderInfo = this.getNoPayOrderByProductId(productId);
        if (orderInfo != null) {
            baseMapper.delete(new LambdaQueryWrapper<OrderInfo>()
                    .eq(OrderInfo::getProductId,productId));
            // return orderInfo;
        }
        //查询商品信息
        Product product = productMapper.selectById(productId);
        // OrderInfo orderInfo = new OrderInfo();
        Optional.ofNullable(product)
                .map(p->{
                    orderInfo.setProductId(productId);
                    orderInfo.setOrderNo(OrderNoUtils.getOrderNo());
                    orderInfo.setTitle(p.getTitle());
                    orderInfo.setOrderStatus(OrderStatus.NOTPAY.getType());
                    orderInfo.setTotalFee(p.getPrice());
                    orderInfo.setIsDeleted(0);
                    baseMapper.insert(orderInfo);
                    return orderInfo;
                })
                .orElseThrow(()->new Exception("用户不存在"));
        return orderInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCodeUrl(String orderNo, String codeUrl) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCodeUrl(codeUrl);
        baseMapper.update(orderInfo, new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getOrderNo, orderNo));
    }

    @Override
    public List<OrderInfo> getOrderListByCreateTime() {
        return baseMapper.selectList(new LambdaQueryWrapper<OrderInfo>()
                .orderByDesc(OrderInfo::getCreateTime));
    }

    /**
     * 更新订单状态
     * @param orderNo
     * @param orderStatus
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    public void updateStatusByOrderNo(String orderNo, OrderStatus orderStatus) {
        log.info("更新订单状态 ===> {}", orderStatus.getType());
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderStatus(orderStatus.getType());
        baseMapper.update(orderInfo, new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getOrderNo,orderNo));
    }

    /**
     * 根据订单号获取订单状态
     * @param orderNo
     * @return
     */
    @Override
    public String getOrderStatus(String orderNo) {
        OrderInfo orderInfo = baseMapper.selectOne(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getOrderNo, orderNo));

        if (orderInfo == null) {
            return null;
        }
        return orderInfo.getOrderStatus();
    }

    private OrderInfo getNoPayOrderByProductId(Long productId) {
        if (productId == null) {
            throw new RuntimeException("订单id为空");
        }

        OrderInfo orderInfo = baseMapper.selectOne(new LambdaQueryWrapper<OrderInfo>()
                .eq(OrderInfo::getProductId, productId)
                .eq(OrderInfo::getOrderStatus, OrderStatus.NOTPAY.getType()));
        return orderInfo;
    }
}
