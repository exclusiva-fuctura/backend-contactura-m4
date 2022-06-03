package br.com.fuctura.contactura.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fuctura.contactura.entities.UserSystem;
import br.com.fuctura.contactura.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	public Optional<UserSystem> getByEmail(String email) {
		return this.repository.findById(email);
	}

	
}
