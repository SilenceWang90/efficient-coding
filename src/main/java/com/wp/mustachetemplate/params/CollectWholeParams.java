package com.wp.mustachetemplate.params;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author wangpeng
 * @Date 2023/10/24 18:44
 */
@Component
public class CollectWholeParams implements InitializingBean {
    @Resource
    private ApplicationContext applicationContext;

    private List<BaseConfigParams> baseConfigParamsList = Lists.newArrayList();

    public List<BaseConfigParams> getBaseConfigParamsList() {
        return baseConfigParamsList;
    }


    @Override
    public void afterPropertiesSet() {
        Map<String, BaseConfigParams> beans = applicationContext.getBeansOfType(BaseConfigParams.class);
        beans.forEach((key, value) -> {
            baseConfigParamsList.add(value);
        });
    }

}
