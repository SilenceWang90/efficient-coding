package com.wp;

import com.wp.lambda.cart.CartService;
import com.wp.lambda.cart.Sku;
import com.wp.lambda.cart.SkuBooksCategoryPredicate;
import com.wp.lambda.cart.SkuPredicate;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Classname Test
 * @Description TODO
 * @Date 2021/1/5 15:29
 * @Created by wangpeng116
 */
@Slf4j
public class Test {

    public void filterSkus() {
        List<Sku> cartSkuList = CartService.getCartSkuList();
        SkuPredicate skuPredicate = new SkuBooksCategoryPredicate();
        List<Sku> result = CartService.filterSku(cartSkuList, skuPredicate);

    }

}
