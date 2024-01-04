package com.example.notificationservice.config;

import com.example.notificationservice.repository.CustomerRepository;
import org.apache.geode.cache.RegionShortcut;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.client.SocketFactory;
import org.apache.geode.cache.client.proxy.ProxySocketFactories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.cache.config.EnableGemfireCaching;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.util.StringUtils;

@Configuration(proxyBeanMethods = false)
@ClientCacheApplication
@EnableGemfireCaching
@EnableGemfireRepositories(basePackageClasses = CustomerRepository.class)
//@EnablePool(name = "sniPool", socketFactoryBeanName = "myProxySocketFactory")
@EnablePdx
@EnableCachingDefinedRegions(clientRegionShortcut = ClientRegionShortcut.PROXY, serverRegionShortcut = RegionShortcut.REPLICATE/* , poolName = "sniPool"*/)
//@EnableEntityDefinedRegions(clientRegionShortcut = ClientRegionShortcut.PROXY, serverRegionShortcut = RegionShortcut.REPLICATE/* , poolName = "sniPool"*/)
//@EnableClusterConfiguration
public class GemFireConfig {

  private static final Logger logger = LoggerFactory.getLogger(GemFireConfig.class);

  @Bean
  SocketFactory myProxySocketFactory(@Value("${cache.host}") String host, @Value("${cache.port}") int port) {
    if (port > 0 && StringUtils.hasText(host)) {
      logger.info("Connecting to GemFire load balancer proxy at {}:{}", host, port);
      return ProxySocketFactories.sni(host, port);
    }
    return SocketFactory.DEFAULT;
  }
}
