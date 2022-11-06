package tk.bolaodacopa.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tk.bolaodacopa.accounts.models.EmailAllowed;

@Repository
public interface EmailAllowedRepository  extends JpaRepository<EmailAllowed, Long> {
	
	Boolean existsByEmail(String email);

}
