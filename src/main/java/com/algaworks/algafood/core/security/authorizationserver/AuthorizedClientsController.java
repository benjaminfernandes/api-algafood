package com.algaworks.algafood.core.security.authorizationserver;

import java.security.Principal;
import java.util.List;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthorizedClientsController {

	private final Oauth2AuthorizationQueryService authorizationQueryService;
	
	@GetMapping("/oauth2/authorized-clients")
	public String clientsList(Principal principal, Model model) {
		List<RegisteredClient> clients = this.authorizationQueryService.listClientsWithConsent(principal.getName());
		model.addAttribute("clients", clients);
		
		return "pages/authorized-clients";
	}
	
}
