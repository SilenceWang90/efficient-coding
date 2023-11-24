package com.wp.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

/**
 * @Description
 * @Author wangpeng
 * @Date 2023/11/24 14:18
 */
@Mapper(componentModel = "spring")
public interface PersonToFatherMapper {

    /**
     * 添加映射规则，如果不添加则表示source和target对象属性名称相同的才会赋值成功
     **/
    /*@Mappings({
            @Mapping(source = "name", target = "fullName"),
            @Mapping(source = "newAge", target = "oldAge"),
            // 其他映射规则...
    })*/

    // 不声明则按照属性名称相同的进行赋值处理
    @Mappings(value = {})
    Father personToFather(Person person);
}
