package com.wp.lambda.cart;

/**
 * @Classname SkuBooksCategoryPredicate
 * @Description 对Sku的商品类型为图书类的判断标准(策略模式实现一)
 * @Date 2021/1/5 15:22
 * @Created by wangpeng116
 */
public class SkuBooksCategoryPredicate implements SkuPredicate {

    @Override
    public boolean test(Sku sku) {
        return SkuCategoryEnum.BOOKS.equals(sku.getSkuCategory());
    }
}
