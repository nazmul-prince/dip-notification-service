package com.dip.unifiedviewer.configuration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * Redis Client configuration for this service.
 *
 * @author Md Sadman Sakib
 */
@Configuration
public class RedissonClientConfig {

  private static final Logger logger = LoggerFactory.getLogger(RedissonClientConfig.class);

  @Value("${spring.redis.host}")
  private String host;

  @Value("${spring.redis.port}")
  private String port;

  @Value("${spring.redis.password}")
  private String password;

  @Value("${dip.redis.subscription-connection-pool-size}")
  private Integer subscriptionConnectionPoolSize;

  @Value("${dip.redis.subscriptions-per-connection}")
  private Integer subscriptionsPerConnection;

  @Value("${dip.redis.subscription-connection-idle-pool-size}")
  private Integer subscriptionsConnectionIdlePoolSize;

  @Value("${dip.redis.connection-pool-size}")
  private Integer connectionPoolSize;

  @Value("${dip.redis.connection-pool-idle-size}")
  private Integer connectionPoolIdleSize;

  @Bean
  public RedissonClient redissonClient() {
    Config config = new Config();
    SingleServerConfig singleServerConfig =
        config
            .useSingleServer()
            .setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
            .setSubscriptionsPerConnection(subscriptionsPerConnection)
            .setSubscriptionConnectionMinimumIdleSize(subscriptionsConnectionIdlePoolSize)
            .setConnectionPoolSize(connectionPoolSize)
            .setConnectionMinimumIdleSize(connectionPoolIdleSize);
    if (!Objects.equals(password, "NONE")) {
      singleServerConfig.setAddress("redis://" + host + ":" + port).setPassword(password);
    } else {
      singleServerConfig.setAddress("redis://" + host + ":" + port);
    }
    logger.info(
        "subscriptionConnectionPoolSize: "
            + subscriptionConnectionPoolSize
            + " and subscriptionsPerConnection: "
            + subscriptionsPerConnection);
    return Redisson.create(config);
  }
}
