package com.wp;

import com.wp.lambda.cart.CartService;
import com.wp.lambda.cart.Sku;
import com.wp.lambda.cart.SkuBooksCategoryPredicate;
import com.wp.lambda.cart.SkuPredicate;
import com.wp.lambda.file.FileService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * @Classname Test
 * @Description TODO
 * @Date 2021/1/5 15:29
 * @Created by wangpeng116
 */
@Slf4j
public class Test {

    @org.junit.Test
    public void filterSkus() {
        List<Sku> cartSkuList = CartService.getCartSkuList();
        SkuPredicate skuPredicate = new SkuBooksCategoryPredicate();
        List<Sku> result = CartService.filterSku(cartSkuList, skuPredicate);
        log.info("结果为：{}", result);
    }

    @org.junit.Test
    public void fileTest() throws IOException {
        FileService fileService = new FileService();
        fileService.fileHandler("E:\\efficient-coding\\src\\test\\java\\com\\wp\\Test.java", fileContent -> {
            System.out.println(fileContent);
        });
    }


}
