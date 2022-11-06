package tk.bolaodacopa.accounts.controllers;

import javax.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import tk.bolaodacopa.accounts.services.AccountService;
import tk.bolaodacopa.accounts.services.EmailAllowedService;
import tk.bolaodacopa.accounts.models.EmailAllowed;
import tk.bolaodacopa.accounts.payload.request.EmailAllowedRequest;
import tk.bolaodacopa.accounts.payload.request.LoginRequest;
import tk.bolaodacopa.accounts.payload.request.SignupRequest;

@RestController
@RequestMapping(path = "/api/auth")
public class AccountController {

	private final AccountService service;
	private final EmailAllowedService serviceEmailAllowed;

	@Autowired
	public AccountController(AccountService service, EmailAllowedService serviceEmailAllowed) {
		this.service = service;
		this.serviceEmailAllowed = serviceEmailAllowed;
	}	

	@PostMapping("/signin")
	public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
		return service.authenticate(loginRequest);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signUpRequest) {
		return service.register(signUpRequest); 
	}
	
	@PostMapping("/emailallowed")
	public ResponseEntity<?> registerEmailAllowed(@RequestHeader("x-emailallowed-pass") String password, 
			@Valid @RequestBody EmailAllowedRequest emailAllowedRequest) {
		return serviceEmailAllowed.register(password, emailAllowedRequest);
	}
	
	@GetMapping("/emailallowed")
	@ResponseStatus(HttpStatus.OK)
	public List<EmailAllowed> findAll(@RequestHeader("x-emailallowed-pass") String password) {
		return serviceEmailAllowed.findAll(password);
	}

}
