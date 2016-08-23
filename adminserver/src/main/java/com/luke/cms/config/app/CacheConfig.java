package com.luke.cms.config.app;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.util.CollectionUtils;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

/**
 * The configure of Cache module
 * @author luke
 */
@Configuration
public class CacheConfig {
    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);
    
    @Value("${cache.redis.host}")
    private String redisHost;
    
    @Value("${cache.redis.port}")
    private int redisPort;
    
//    @Value("${front.redis.sentinel.hosts}")
//    private String sentinelHosts;
//    
//    @Value("${front.redis.sentinel.mastername}")
//    private String masterName;

    @Bean(name = "myJedisPool")
    public JedisPool jedisPool() {
        logger.info("Connect redis url: "+redisHost+":"+redisPort);
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxWaitMillis(60000);
        config.setMaxTotal(100);
        config.setMinIdle(8);
        config.setTestOnBorrow(true);
        config.setTestWhileIdle(true);
        return new JedisPool(config, redisHost, redisPort);
    }
    
    // sentine model
//    @Bean(name = "myJedisSentinelPool")
//    public JedisSentinelPool jedisPool() {
//        logger.info("sentinel master name: "+masterName +" hosts: "+sentinelHosts);
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxWaitMillis(60000);
//        config.setMaxTotal(1000);
//        config.setMinIdle(100);
//        config.setTestOnBorrow(true);
//        config.setTestWhileIdle(true);
//        
//        Set<String> sentinels = new HashSet<String>();
//        sentinels.addAll(CollectionUtils.arrayToList(sentinelHosts.split(",")));
//        return new JedisSentinelPool(masterName, sentinels, config);
//    }
}
