package com.algaworks.algafood.core.security.authorizationserver;

import java.util.List;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

public class JdbcOauth2AuthorizationQueryService implements Oauth2AuthorizationQueryService{

	private final JdbcOperations operations;
	private final RowMapper<RegisteredClient> registeredClientRowMapper;
	private final String LIST_AUTHORIZED_CLIENTS = "select rc.* from oauth2_authorization_consent c inner join oauth2_registered_client rc on rc.id = c.registered_client_id"
			+ " where c.principal_name = ? ";
	
	public JdbcOauth2AuthorizationQueryService(JdbcOperations operations) {
		this.operations = operations;
		this.registeredClientRowMapper = new JdbcRegisteredClientRepository.RegisteredClientRowMapper();
	}

	@Override
	public List<RegisteredClient> listClientsWithConsent(String principalName) {
		return this.operations.query(LIST_AUTHORIZED_CLIENTS, registeredClientRowMapper, principalName);
	}

	
}
