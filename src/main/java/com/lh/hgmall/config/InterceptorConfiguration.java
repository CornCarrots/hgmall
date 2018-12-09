package com.lh.hgmall.config;

import com.lh.hgmall.interceptor.BackInterceptor;
import com.lh.hgmall.interceptor.ForeInterceptor;
import com.lh.hgmall.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class InterceptorConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public HandlerInterceptor getForeInterceptor(){
        return new ForeInterceptor();
    }
    @Bean
    public HandlerInterceptor getBackInterceptor(){ return new BackInterceptor(); }
    @Bean
    public HandlerInterceptor getLoginInterceptor(){ return new LoginInterceptor(); }


    @Override
    public void addInterceptors(InterceptorRegistry registry){

        //注册拦截器
        InterceptorRegistration foreRegistration = registry.addInterceptor(getForeInterceptor());
        //配置拦截路径
        foreRegistration.addPathPatterns("/**");
        //配置不拦截的路径
        foreRegistration.excludePathPatterns("/**/admin/**");
        foreRegistration.excludePathPatterns("/foreHome");
        foreRegistration.excludePathPatterns("/foreHelp");
        foreRegistration.excludePathPatterns("/foreMessage");
        foreRegistration.excludePathPatterns("/foreView");
        foreRegistration.excludePathPatterns("/foreMember");
        foreRegistration.excludePathPatterns("/foreLoginImage");
        foreRegistration.excludePathPatterns("/foreLoginCheck");
        foreRegistration.excludePathPatterns("/foreAccountCheck");
        foreRegistration.excludePathPatterns("/foreLoginUser");
        foreRegistration.excludePathPatterns("/foreLoginStore");
        foreRegistration.excludePathPatterns("/foreApplyStore");
        foreRegistration.excludePathPatterns("/foreRegister");
        foreRegistration.excludePathPatterns("/foreBuyProduct");
        foreRegistration.excludePathPatterns("/foreAddCart");
        foreRegistration.excludePathPatterns("/checkUser");
        foreRegistration.excludePathPatterns("/foreStore/**");
        foreRegistration.excludePathPatterns("/foreSearch/**");
        foreRegistration.excludePathPatterns("/foreCategory/**");
        foreRegistration.excludePathPatterns("/foreProduct/**");
        foreRegistration.excludePathPatterns("/forePower/**");
        foreRegistration.excludePathPatterns("/foreOrderItem/**");
        foreRegistration.excludePathPatterns("/foreLogistics/**");
        foreRegistration.excludePathPatterns("/foreOrder/**");
        foreRegistration.excludePathPatterns("/forePayOrder/**");
        foreRegistration.excludePathPatterns("/foreConfirmOrder/**");
        foreRegistration.excludePathPatterns("/foreReview/**");
        foreRegistration.excludePathPatterns("/foreReviewSuccess/**");
        foreRegistration.excludePathPatterns("/foreExchangeOrder/**");
        foreRegistration.excludePathPatterns("/foreUser/**");
        foreRegistration.excludePathPatterns("/foreOrder");
        foreRegistration.excludePathPatterns("/foreOrderList");
        foreRegistration.excludePathPatterns("/forePayOrder");
        foreRegistration.excludePathPatterns("/foreConfirmOrder");
        foreRegistration.excludePathPatterns("/foreUser");
        foreRegistration.excludePathPatterns("/foreOpenAccount");
        foreRegistration.excludePathPatterns("/foreRecharge");
        foreRegistration.excludePathPatterns("/forgetUser");
        foreRegistration.excludePathPatterns("/foreUserPass");
        foreRegistration.excludePathPatterns("/forgetManager");
        foreRegistration.excludePathPatterns("/foreManagerPass");

        //注册拦截器
        InterceptorRegistration loginRegistration = registry.addInterceptor(getLoginInterceptor());
        //配置拦截路径
        loginRegistration.addPathPatterns("/member/**");
        loginRegistration.addPathPatterns("/shoppingcart/**");
        loginRegistration.addPathPatterns("/user/**");
        loginRegistration.addPathPatterns("/account/**");
        loginRegistration.addPathPatterns("/view/**");
        loginRegistration.addPathPatterns("/orderList/**");
        //配置不拦截的路径

        //注册拦截器
        InterceptorRegistration BackRegistration = registry.addInterceptor(getBackInterceptor());
        //配置拦截路径
        BackRegistration.addPathPatterns("/**/admin/**");
        //配置不拦截的路径
    }
}
