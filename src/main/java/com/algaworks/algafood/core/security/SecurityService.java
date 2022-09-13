package com.algaworks.algafood.core.security;

import java.util.Arrays;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

//Uma opção mais "programática" para checar as authorities
@Component
public class SecurityService {

	public boolean listarCozinhas() {
		return hasAuthority("ACESSO_A_COZINHAS");
	}
	
	public boolean cadastrarCozinhas() {
		return listarCozinhas() && hasAuthority("CRIAR_COZINHA");
	}
	
	public boolean isAuthenticated() {
		return getAuthentication().isAuthenticated();
	}
	
	public boolean hasAuthority(String authorityName) {
		return getAuthentication().getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(authorityName));
	}
	
	public boolean hasAnyAuthority(String... authorityName) {
		return getAuthentication().getAuthorities().stream()
				.anyMatch(authority -> Arrays.asList(authorityName).contains(authority.getAuthority()));
	}
	
	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
}
