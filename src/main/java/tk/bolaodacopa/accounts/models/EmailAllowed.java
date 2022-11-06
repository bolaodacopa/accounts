package tk.bolaodacopa.accounts.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "emailsallowed", 
uniqueConstraints = { 
		@UniqueConstraint(columnNames = "email") 
})
public class EmailAllowed {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(max = 50)
	@Email
	private String email;
	
	public EmailAllowed() {
		super();
	}

	public EmailAllowed(@NotBlank @Size(max = 50) @Email String email) {
		super();
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	
	
}
