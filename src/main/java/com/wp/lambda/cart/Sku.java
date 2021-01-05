package com.wp.lambda.cart;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Classname Sku
 * @Description 下单商品信息对象
 * @Date 2021/1/5 14:29
 * @Created by wangpeng116
 */
@Data
public class Sku {
    //编号
    private Integer skuId;
    //名称
    private String skuName;
    //单价
    private BigDecimal skuPrice;
    //购买个数
    private Integer totalNum;
    //总价
    private BigDecimal totalPrice;
    //商品类型
    private Enum skuCategory;
}
