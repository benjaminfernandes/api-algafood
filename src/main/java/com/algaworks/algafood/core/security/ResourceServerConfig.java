package com.algaworks.algafood.core.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) //para funcionar os filters
@EnableWebSecurity
public class ResourceServerConfig {

	
	@Bean
	public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
		
		http
			.formLogin(Customizer.withDefaults())//esta config somente é necessário de o Authorization server é o mesmo projeto do resource server 
			.csrf().disable()
			.cors().and()
			//.oauth2ResourceServer().opaqueToken(); quando é utilizado o token opaco
			.oauth2ResourceServer()
			.jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
		
		//return http.formLogin(Customizer.withDefaults())//É também adicionada esta config aqui pois o AS está no mesmo projeto do Resource Server
				//.build();
		
		return http.build();
	}
	
	//Esta config foi feita para resolver o problema demonstrado na aula 27.12 que alterou o retorno do método loadUserByUsername da classe JpaUserDetailsService
	//Aqui ele adiciona os authorities 
	 private JwtAuthenticationConverter jwtAuthenticationConverter() {
	        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

	        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
	            List<String> authorities = jwt.getClaimAsStringList("authorities");

	            if (authorities == null) {
	                return Collections.emptyList();
	            }

	            JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
	            Collection<GrantedAuthority> grantedAuthorities = authoritiesConverter.convert(jwt);

	            grantedAuthorities.addAll(authorities
	                    .stream()
	                    .map(SimpleGrantedAuthority::new)
	                    .collect(Collectors.toList()));

	            return grantedAuthorities;
	        });

	        return converter;
	    }
}
