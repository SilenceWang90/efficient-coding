package com.wp.lambda.cart;

/**
 * @Classname SkuPredicate
 * @Description Sku选择谓词接口
 * @Date 2021/1/5 15:16
 * @Created by wangpeng116
 */
public interface SkuPredicate {

    /**
     * 选择判断标准
     *
     * @param sku
     * @return
     */
    boolean test(Sku sku);
}
