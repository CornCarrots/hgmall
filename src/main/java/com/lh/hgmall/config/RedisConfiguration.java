package com.lh.hgmall.config;

import com.alibaba.fastjson.parser.ParserConfig;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lh.hgmall.util.FastJsonRedisSerializer;
import org.apache.catalina.Session;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.io.Serializable;
import java.lang.reflect.Method;

@Configuration
@EnableCaching
public class RedisConfiguration extends CachingConfigurerSupport {
    /*定义缓存数据 key 生成策略的bean
    包名+类名+方法名+所有参数
    */
    @Bean
    public KeyGenerator wiselyKeyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
//                sb.append(target.getClass().getSimpleName().substring(0,target.getClass().getSimpleName().lastIndexOf("Service")));
                sb.append(method.getName()+" ");
                for (int i = 0; i < params.length; i++) {
                    if (i!=params.length-1)
                        sb.append(params[i].toString()+"-");
                    else
                        sb.append(params[i].toString());
                }
                return sb.toString();
            }
        };

    }
//    @Bean
//    public CacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate<?, ?> redisTemplate) {
//        // 创建缓存管理器
//        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
//        cacheManager.setDefaultExpiration(3600 * 24);
//        cacheManager.setUsePrefix(true);
//        cacheManager.setCachePrefix(new DefaultRedisCachePrefix(":"));
//        return cacheManager;
//    }
////
//    @Bean
//    public RedisTemplate redisTemplate(JedisConnectionFactory redisConnectionFactory) {
//        // 1.创建 redisTemplate 模版
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        // 2.关联 redisConnectionFactory
//        template.setConnectionFactory(redisConnectionFactory);
//        // 3.创建 序列化类
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        // 4.设置可见度
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        // 5.启动默认的类型
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        // 6.序列化类，对象映射设置
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        // 7.设置 value 的转化格式和 key 的转化格式
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.afterPropertiesSet();
//        return template;
//    }
//
//    @Bean
//    public RedisTemplate sessionRedisTemplate(JedisConnectionFactory factory) {
//        RedisTemplate<Serializable, Session> template = new RedisTemplate();
//        template.setConnectionFactory(factory);
//        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new JdkSerializationRedisSerializer());
//        template.afterPropertiesSet();
//        return template;
//    }

//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
//        // 全局开启AutoType，不建议使用
//         ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
//        // 建议使用这种方式，小范围指定白名单
////        ParserConfig.getGlobalInstance().addAccept("com.lh");
//
//        // 设置值（value）的序列化采用FastJsonRedisSerializer。
//        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
//        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
//        // 设置键（key）的序列化采用StringRedisSerializer。
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }

    @Bean
    public CacheManager cacheManager(RedisTemplate<?,?> redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.PUBLIC_ONLY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);

        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);

        // 开启使用缓存名称为key前缀
        cacheManager.setUsePrefix(true);
        cacheManager.setCachePrefix(new DefaultRedisCachePrefix(":"));
        //设置缓存过期时间
        cacheManager.setDefaultExpiration(3600 * 24);
        return cacheManager;

    }
}
