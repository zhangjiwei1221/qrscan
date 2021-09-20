package cn.butterfly.qrscan.config;

import cn.butterfly.qrscan.filter.HttpInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import static cn.butterfly.qrscan.constant.BaseConstants.*;

/**
 * web 配置
 *
 * @author zjw
 * @date 2021-09-09
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

	private final HttpInterceptor httpInterceptor;

	public WebConfig(HttpInterceptor httpInterceptor) {
		this.httpInterceptor = httpInterceptor;
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping(ALL_PATTERN)
				.allowedOrigins(ALL)
				.allowedMethods(HttpMethod.POST.name(), HttpMethod.GET.name(), HttpMethod.OPTIONS.name())
				.exposedHeaders(AUTHORIZATION)
				.allowedHeaders(ALL);
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(RESOURCE_PATTERN)
				.addResourceLocations(RESOURCE_LOCATION);
		super.addResourceHandlers(registry);
	}

	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(httpInterceptor)
				.addPathPatterns(API_PATTERN)
				.excludePathPatterns(LOGIN_PATH, QR_CODE_PREFIX, ERROR_PATH);
		super.addInterceptors(registry);
	}

}
