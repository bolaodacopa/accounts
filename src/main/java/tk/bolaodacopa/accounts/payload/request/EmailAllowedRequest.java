package tk.bolaodacopa.accounts.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EmailAllowedRequest {
	
	@NotBlank(message = "O email deve ser informado!")
	@Size(max = 50, message = "O email deve ter no máximo 50 caracteres!")
	@Email(message = "Informe um email válido!")
	private String email;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}	

}
