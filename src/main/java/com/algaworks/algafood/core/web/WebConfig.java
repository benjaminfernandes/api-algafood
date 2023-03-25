package com.algaworks.algafood.core.web;

import jakarta.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private ApiRetirementHandler apiRetirementHandler;
	
	/*
	 * Removido na aula 23.41 - implementado na classe CorsConfig
	 * @Override
	 
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			//.allowedOrigins("http://localhost:8000");
			.allowedMethods("*");
			//.maxAge(30) //em segundos
	}
	*/
	//Aula 17.5 ESR
	@Bean
	public Filter shallowEtagHeaderFilter() {
		return new ShallowEtagHeaderFilter();
	}
	
	//intercepta todas as chamadas aos controladores da api
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(this.apiRetirementHandler);
	}
}
