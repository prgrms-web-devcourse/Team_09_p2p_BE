package com.prgrms.p2p.domain.user.config.config;


import com.prgrms.p2p.domain.common.log.LogInterceptor;
import com.prgrms.p2p.domain.user.aop.CurrentUserArgumentResolver;
import com.prgrms.p2p.domain.user.config.security.RedisInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

  private final LogInterceptor logInterceptor;
  private final RedisInterceptor permissionInterceptor;
  private final CurrentUserArgumentResolver currentUserArgumentResolver;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(permissionInterceptor)
        .addPathPatterns("/api/v1/users/login");
    registry.addInterceptor(logInterceptor)
        .addPathPatterns("/**");
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(currentUserArgumentResolver);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("*");
  }
}