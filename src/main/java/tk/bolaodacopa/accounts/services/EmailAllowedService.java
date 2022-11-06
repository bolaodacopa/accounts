package tk.bolaodacopa.accounts.services;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import tk.bolaodacopa.accounts.models.EmailAllowed;
import tk.bolaodacopa.accounts.payload.request.EmailAllowedRequest;
import tk.bolaodacopa.accounts.payload.response.MessageResponse;
import tk.bolaodacopa.accounts.repository.EmailAllowedRepository;

@Service
public class EmailAllowedService {

	@Autowired
	EmailAllowedRepository emailAllowedRepository;

	public ResponseEntity<?> register(String password, @Valid EmailAllowedRequest emailAllowedRequest) {
		authenticate(password);
		if (emailAllowedRepository.existsByEmail(emailAllowedRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Erro: Email já está em uso!"));
		}		

		EmailAllowed emailAllowed = new EmailAllowed(emailAllowedRequest.getEmail());

		emailAllowedRepository.save(emailAllowed);

		return ResponseEntity.ok(new MessageResponse("Email cadastrado com sucesso!"));
	}

	public List<EmailAllowed> findAll(String password) {
		authenticate(password);
		return emailAllowedRepository.findAll();
	}

	private boolean authenticate(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA3-256");
			byte[] result = md.digest(password.getBytes(StandardCharsets.UTF_8));	

			StringBuilder sb = new StringBuilder();
			for (byte b : result) {
				sb.append(String.format("%02x", b));
			}
			String hash = sb.toString();
			if("0fe7e9d40d603370fcafa71f55b410e65fa61f1f9a4fb0131f3a02c05a47ffcd".equals(hash))
				return true;
		} catch(Exception e) {}

		throw new RuntimeException("Erro: Senha inválida.");
	}

}
