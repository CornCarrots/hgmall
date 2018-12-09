package com.lh.hgmall.config;

import com.lh.hgmall.filter.URLPathMatchingFilter;
import com.lh.hgmall.realm.DatabaseRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    //    保证实现了Shiro内部lifecycle函数的bean执行
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    //    配置shiro的过滤器工厂类
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        System.out.println("ShiroConfiguration.shiroFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();

        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/store_login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/admin");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
        //拦截器.
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        //自定义拦截器
        Map<String, Filter> customisedFilter = new HashMap<>();
        customisedFilter.put("url", getURLPathMatchingFilter());

        //配置映射关系
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/image/**", "anon");

        filterChainDefinitionMap.put("/unauthorized", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/register", "anon");
        filterChainDefinitionMap.put("/logout", "logout");

        filterChainDefinitionMap.put("/registerSuccess", "anon");
        filterChainDefinitionMap.put("/home", "anon");
        filterChainDefinitionMap.put("/category", "anon");
        filterChainDefinitionMap.put("/product", "anon");
        filterChainDefinitionMap.put("/store", "anon");
        filterChainDefinitionMap.put("/user", "anon");
        filterChainDefinitionMap.put("/account", "anon");
        filterChainDefinitionMap.put("/orderList", "anon");
        filterChainDefinitionMap.put("/shoppingcart", "anon");
        filterChainDefinitionMap.put("/help", "anon");
        filterChainDefinitionMap.put("/message", "anon");
        filterChainDefinitionMap.put("/view", "anon");
        filterChainDefinitionMap.put("/member", "anon");
        filterChainDefinitionMap.put("/power", "anon");
        filterChainDefinitionMap.put("/manager", "anon");
        filterChainDefinitionMap.put("/store_apply", "anon");
        filterChainDefinitionMap.put("/store_applySuccess", "anon");
        filterChainDefinitionMap.put("/orderItem", "anon");
        filterChainDefinitionMap.put("/order_pay", "anon");
        filterChainDefinitionMap.put("/order_confirm", "anon");
        filterChainDefinitionMap.put("/order_review", "anon");
        filterChainDefinitionMap.put("/waitSuccess", "anon");
        filterChainDefinitionMap.put("/paySuccess", "anon");
        filterChainDefinitionMap.put("/confirmSuccess", "anon");
        filterChainDefinitionMap.put("/reviewSuccess", "anon");


        filterChainDefinitionMap.put("/admin/**", "url");
        shiroFilterFactoryBean.setFilters(customisedFilter);
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

//    url过滤器
    public URLPathMatchingFilter getURLPathMatchingFilter() {
        return new URLPathMatchingFilter();
    }

    //    安全管理器
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        //设置realm.
        securityManager.setRealm(getDatabaseRealm());
        return securityManager;
    }

//     注入realm
    @Bean
    public DatabaseRealm getDatabaseRealm(){
        DatabaseRealm myShiroRealm = new DatabaseRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     *  所以我们需要修改下doGetAuthenticationInfo中的代码;
     * ）
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));

        return hashedCredentialsMatcher;
    }


    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
