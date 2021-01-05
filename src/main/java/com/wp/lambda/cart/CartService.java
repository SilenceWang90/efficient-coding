package com.wp.lambda.cart;

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Classname CartService
 * @Description 购物车服务类
 * @Date 2021/1/5 14:35
 * @Created by wangpeng116
 */
public class CartService {
    //加入到购物车中的商品
    private static List<Sku> cartSkuList = Lists.newArrayList(
            new Sku(654032, "无人机", BigDecimal.valueOf(4999.00), 1, BigDecimal.valueOf(4999.00), SkuCategoryEnum.ELECTRONICS)
            , new Sku(642934, "VR一体机", BigDecimal.valueOf(2299.00), 1, BigDecimal.valueOf(2299.00), SkuCategoryEnum.ELECTRONICS)
            , new Sku(645321, "纯色衬衫", BigDecimal.valueOf(409.00), 3, BigDecimal.valueOf(1227.00), SkuCategoryEnum.CLOTHING)
            , new Sku(645327, "牛仔裤", BigDecimal.valueOf(528.00), 1, BigDecimal.valueOf(528.00), SkuCategoryEnum.CLOTHING)
            , new Sku(675489, "跑步机", BigDecimal.valueOf(2699.00), 1, BigDecimal.valueOf(2699.00), SkuCategoryEnum.SPORTS)
            , new Sku(644564, "Java编程思想", BigDecimal.valueOf(79.80), 1, BigDecimal.valueOf(79.80), SkuCategoryEnum.BOOKS)
            , new Sku(678678, "Java核心技术", BigDecimal.valueOf(149.00), 1, BigDecimal.valueOf(149.00), SkuCategoryEnum.BOOKS)
            , new Sku(697894, "算法", BigDecimal.valueOf(78.20), 1, BigDecimal.valueOf(78.20), SkuCategoryEnum.BOOKS)
            , new Sku(696968, "TensorFlow进阶指南", BigDecimal.valueOf(85.10), 1, BigDecimal.valueOf(85.10), SkuCategoryEnum.BOOKS)
    );
}
