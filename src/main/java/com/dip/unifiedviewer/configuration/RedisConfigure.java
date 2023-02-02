package com.dip.unifiedviewer.configuration;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.JedisPoolingClientConfigurationBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.core.resource.DefaultClientResources;
import redis.clients.jedis.JedisPoolConfig;

/**
 * This class creates beans for the redis connection.
 * 
 * @author dev
 *
 */
@Configuration
public class RedisConfigure {

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private String port;

	@Value("${spring.redis.password}")
	private String password;

	@Value("${spring.redis.profile.active}")
	private String redisConnectionImplType;

	@Bean
	public RedisConnectionFactory redisPublisherConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host,
				Integer.parseInt(port));

		if (!Objects.equals(password, "NONE"))
			redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

		RedisConnectionFactory redisConnectionFactory;
		
		if(Objects.equals(redisConnectionImplType, "jedis")) {
			redisConnectionFactory = new JedisConnectionFactory(
					redisStandaloneConfiguration,
					jedisClientConfiguration());
		} else {
			redisConnectionFactory = new LettuceConnectionFactory(
					redisStandaloneConfiguration,
					lettucePoolingClientConfiguration());
		}
		return redisConnectionFactory;
	}
	
	@Bean
	public JedisClientConfiguration jedisClientConfiguration() {

		JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpccb =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder().usePooling();

        jpccb.poolConfig(jedisPoolConfig());

        JedisClientConfiguration jedisClientConfiguration = jpccb.build();
        return jedisClientConfiguration;
	}
	
	@Bean(destroyMethod = "shutdown")
	ClientResources clientResources() {
	    return DefaultClientResources.create();
	}
	
	@Bean
	public ClientOptions clientOptions(){
	    return ClientOptions.builder()
	            .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
	            .autoReconnect(true)
	            .build();
	}
	
	@Bean
	LettucePoolingClientConfiguration lettucePoolingClientConfiguration(){
	    return LettucePoolingClientConfiguration.builder()
	            .poolConfig(jedisPoolConfig())
	            .clientOptions(clientOptions())
	            .clientResources(clientResources())
	            .build();
	}
	
	@Bean
	public JedisPoolConfig jedisPoolConfig() {
	    final JedisPoolConfig poolConfig = new JedisPoolConfig();
	    poolConfig.setMaxTotal(20);
	    poolConfig.setMaxIdle(20);
	    return poolConfig;
	  }

	@Bean
	public RedisTemplate<String, Object> redisPublisherTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisPublisherConnectionFactory());
		template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		
		boolean usePool = false, useSsl;
		int maxTotal, maxIdle;
		
		if(Objects.equals(redisConnectionImplType, "jedis")) {
			JedisConnectionFactory connectionFactory = (JedisConnectionFactory) template.getConnectionFactory();
			usePool = connectionFactory.getUsePool();
			maxTotal = connectionFactory.getPoolConfig().getMaxTotal();
			maxIdle = connectionFactory.getPoolConfig().getMaxIdle();
			useSsl = connectionFactory.getClientConfiguration().isUseSsl();
		} else {

			LettuceConnectionFactory connectionFactory = (LettuceConnectionFactory) template.getConnectionFactory();
			LettucePoolingClientConfiguration clientConfiguration = (LettucePoolingClientConfiguration) connectionFactory.getClientConfiguration();
			maxTotal = clientConfiguration.getPoolConfig().getMaxTotal();
			maxIdle = clientConfiguration.getPoolConfig().getMaxIdle();
			useSsl = clientConfiguration.isUseSsl();
		}
		
		System.out.println(redisConnectionImplType + " using pool: " + usePool + " maxTototal: " + maxTotal + " maxIdle: " + maxIdle
				+ " useSsl: " + useSsl);
		return template;
	}

//    @Bean
//    public ChannelTopic topic(){
//        return new ChannelTopic("redisChannel");
//    }

//    @Bean
//    public RedisMessagePublisher messagePublisher(){
//        return new RedisMessagePublisher(redisTemplate());
//        //return new RedisMessagePublisher();
//    }
}
