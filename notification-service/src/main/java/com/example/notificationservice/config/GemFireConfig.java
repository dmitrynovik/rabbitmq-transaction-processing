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

  // ONOBC: Are the commented out annotations above temporary? If not, the recommended way to do this type of thing is via
  // profile controlled/activated config props or configurations.
  //
  // https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.profiles
  //
  //https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.external-config.files.profile-specific
  //
  //https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.external-config.files.multi-document

  // ONOBC: I typically make all loggers private static final and use uppercase naming (LOGGER) for consistency
  private static final Logger LOGGER = LoggerFactory.getLogger(GemFireConfig.class);

  // ONOBC: Please replace all variants of StringUtils w/ org.springframework.util.StringUtils

  @Bean
  SocketFactory myProxySocketFactory(@Value("${cache.host}") String host, @Value("${cache.port}") int port) {
    if (port > 0 && StringUtils.hasText(host)) {
      LOGGER.info("Connecting to GemFire load balancer proxy at " + host + ":" + port);
      return ProxySocketFactories.sni(host, port);
    }
    return SocketFactory.DEFAULT;
  }
}
