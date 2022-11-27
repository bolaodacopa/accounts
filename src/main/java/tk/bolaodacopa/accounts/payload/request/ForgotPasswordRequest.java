package tk.bolaodacopa.accounts.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ForgotPasswordRequest {
	
	@NotBlank(message = "O username deve ser informado!")
	@Size(min = 3, max = 20, message = "O username deve ter entre 3 e 20 caracteres alfanuméricos!")
	@Pattern(regexp = "[a-zA-z0-9]*", message = "O username deve conter apenas caracteres alfanuméricos!")
	private String username;

	@NotBlank(message = "O email deve ser informado!")
	@Size(max = 50, message = "O email deve ter no máximo 50 caracteres!")
	@Email(message = "Informe um email válido!")
	private String email;

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

}
