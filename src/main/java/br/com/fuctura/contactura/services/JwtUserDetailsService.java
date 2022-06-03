package br.com.fuctura.contactura.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.fuctura.contactura.entities.UserSystem;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<UserSystem> obj = userService.getByEmail(email);
		if (obj.isPresent()) {
			UserSystem user = obj.get();
			
			return new User(email, user.getPassword(), new ArrayList<>());
		} else {
			throw new UsernameNotFoundException("Usuario nao encontrado: " + email);
		}
		

		
	}

}
