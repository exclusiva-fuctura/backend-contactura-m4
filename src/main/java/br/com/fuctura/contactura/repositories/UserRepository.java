package br.com.fuctura.contactura.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.fuctura.contactura.entities.UserSystem;

@Transactional(readOnly = true)
@Repository
public interface UserRepository extends JpaRepository<UserSystem, String>{

}
