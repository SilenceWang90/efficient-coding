package com.wp.lambda.cart;

/**
 * @Classname SkuCategoryEnum
 * @Description 商品类型枚举
 * @Date 2021/1/5 14:44
 * @Created by wangpeng116
 */
public enum SkuCategoryEnum {
    CLOTHING(10, "服装类"),
    ELECTRONICS(20, "数码类"),
    SPORTS(30, "运动类"),
    BOOKS(40, "图书类"),
    ;
    //商品类型编号
    private Integer code;
    //商品类型的名称
    private String name;

    SkuCategoryEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
