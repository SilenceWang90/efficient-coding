package com.wp.lambda.cart;

import java.math.BigDecimal;

/**
 * @Classname SkuTotalPricePredicate
 * @Description 对Sku的商品类型为总价的判断标准(策略模式实现一)
 * @Date 2021/1/5 15:24
 * @Created by wangpeng116
 */
public class SkuTotalPricePredicate implements SkuPredicate {
    @Override
    public boolean test(Sku sku) {
        return sku.getTotalPrice().compareTo(BigDecimal.valueOf(2000)) > 0;
    }
}
