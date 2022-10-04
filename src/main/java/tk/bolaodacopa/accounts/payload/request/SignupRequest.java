package tk.bolaodacopa.accounts.payload.request;


import java.util.Set;

import javax.validation.constraints.*;

public class SignupRequest {
	@NotBlank(message = "O username deve ser informado!")
	@Size(min = 3, max = 20, message = "O username deve ter entre 3 e 20 caracteres alfanuméricos!")
	@Pattern(regexp = "[a-zA-z0-9]*", message = "O username deve conter apenas caracteres alfanuméricos!")
	private String username;

	@NotBlank(message = "O nome deve ser informado!")
	@Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres alfanuméricos!")
	private String name;  

	@NotBlank(message = "O email deve ser informado!")
	@Size(max = 50, message = "O email deve ter no máximo 50 caracteres!")
	@Email(message = "Informe um email válido!")
	private String email;

	private Set<String> role;

	@NotBlank(message = "A senha deve ser informado!")
	@Size(min = 6, max = 40)
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRole() {
		return this.role;
	}

	public void setRole(Set<String> role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
