package tk.bolaodacopa.accounts.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tk.bolaodacopa.accounts.services.AccountService;
import tk.bolaodacopa.accounts.payload.request.LoginRequest;
import tk.bolaodacopa.accounts.payload.request.SignupRequest;

@RestController
@RequestMapping(path = "/api/auth")
public class AccountController {

	private final AccountService service;

	@Autowired
	public AccountController(AccountService service) {
		this.service = service;
	}	

	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
		return service.authenticate(loginRequest);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signUpRequest) {
		return service.register(signUpRequest); 
	}
}
