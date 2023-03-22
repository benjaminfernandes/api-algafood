package com.algaworks.algafood.core.security.authorizationserver;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;

@Service
public class JpaUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Transactional(readOnly = true)//Para manter o entityManager aberto, também poderia ser resolvido fazendo um join fetch
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = this.usuarioRepository.findByEmail(username)
					.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
		
		/*Foi alterado o retorno pois com a nova versão o Spring oauth2 ficou com problema na deserialização e cast com o objeto AuthUser
		 * Desta forma o JWT perdeu o acesso dos dados do usuário, tendo que fazer outra rotina para realizar a busca desta informações.
		 * Aula 27.12
		*/
		//return new AuthUser(usuario, getAuthorities(usuario));
		return new User(usuario.getEmail(),usuario.getSenha(), getAuthorities(usuario));
	}

	private Collection<GrantedAuthority> getAuthorities(Usuario usuario){
		return usuario.getGrupos().stream()
			.flatMap(grupo -> grupo.getPermissoes().stream())//flatMap achata os dados para ficar linear, utilizado em listas de listas... neste caso não queremos a lista de grupos, mas sim lista de permissoes
			.map(permissao -> new SimpleGrantedAuthority(permissao.getNome().toUpperCase()))
			.collect(Collectors.toSet());
	}
	
}
