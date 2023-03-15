package com.algaworks.algafood.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiRetirementHandler implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//o if abaixo é para retornar quando a V1 foi totalmente desabilitada do projeto
		/*if(request.getRequestURI().startsWith("/v1/")) {
			//usado para enviar a informação no cabeçalho
			//response.addHeader("X-Algafood-Deprecated", "Versão deprecidada!");
			
			response.setStatus(HttpStatus.GONE.value());
			return false;
		}*/
		
		return true;
	}
}
