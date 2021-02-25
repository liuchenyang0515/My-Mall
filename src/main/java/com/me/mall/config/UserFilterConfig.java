package com.me.mall.config;

import com.me.mall.filter.UserFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * 描述：Admin过滤器的配置
 */
@Configuration
public class UserFilterConfig {

    @Bean
    public UserFilter userFilter() {
        return new UserFilter();
    }

    @Bean(name = "userFilterConf") // 不能设置的和类名一样，会冲突
    public FilterRegistrationBean adminFilterConfig() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        // 设置过滤器到链路中
        filterRegistrationBean.setFilter(userFilter());
        filterRegistrationBean.addUrlPatterns("/cart/*"); // 购物车相关模块进行校验
        filterRegistrationBean.addUrlPatterns("/order/*");// 订单相关模块进行校验
        filterRegistrationBean.setName("userFilterConf"); // 设置这个过滤器的名字
        return filterRegistrationBean;
    }
}
