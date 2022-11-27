package tk.bolaodacopa.accounts.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;
import tk.bolaodacopa.accounts.models.Account;
import tk.bolaodacopa.accounts.models.ERole;
import tk.bolaodacopa.accounts.models.Role;
import tk.bolaodacopa.accounts.payload.request.ForgotPasswordRequest;
import tk.bolaodacopa.accounts.payload.request.LoginRequest;
import tk.bolaodacopa.accounts.payload.request.ResetPasswordRequest;
import tk.bolaodacopa.accounts.payload.request.SignupRequest;
import tk.bolaodacopa.accounts.payload.response.JwtResponse;
import tk.bolaodacopa.accounts.payload.response.MessageResponse;
import tk.bolaodacopa.accounts.repository.AccountRepository;
import tk.bolaodacopa.accounts.repository.EmailAllowedRepository;
import tk.bolaodacopa.accounts.repository.RoleRepository;
import tk.bolaodacopa.accounts.security.jwt.EmailUtils;
import tk.bolaodacopa.accounts.security.jwt.JwtUtils;
import tk.bolaodacopa.accounts.security.services.AccountDetailsImpl;

@Service
public class AccountService {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	EmailAllowedRepository emailAllowedRepository;	

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;	
	
	@Autowired
	EmailUtils emailUtils;

	public ResponseEntity<?> authenticate(@Valid LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		AccountDetailsImpl accountDetails = (AccountDetailsImpl) authentication.getPrincipal();    

		List<String> roles = accountDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, 
				accountDetails.getId(), 
				accountDetails.getUsername(), 
				accountDetails.getName(),
				accountDetails.getEmail(), 
				roles));
	}

	public ResponseEntity<?> register(@Valid SignupRequest signUpRequest) {
		if (accountRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Erro: Username já foi usado!"));
		}

		if (accountRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Erro: Email já está em uso!"));
		}

		if (!(emailAllowedRepository.existsByEmail(signUpRequest.getEmail()))) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Erro: Email não permitido. Contacte o Administrador!"));
		}

		// Create new user's account
		Account account = new Account(signUpRequest.getUsername(), 
				signUpRequest.getName(),
				signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Erro: Perfil não foi encontrado."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
					.orElseThrow(() -> new RuntimeException("Erro: Perfil não foi encontrado."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
					.orElseThrow(() -> new RuntimeException("Erro: Perfil não foi encontrado."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Erro: Perfil não foi encontrado."));
					roles.add(userRole);
				}
			});
		}

		account.setRoles(roles);
		accountRepository.save(account);

		return ResponseEntity.ok(new MessageResponse("Usuário cadastrado com sucesso!"));
	}

	public ResponseEntity<?> forgotPassword(@Valid ForgotPasswordRequest forgotPasswordRequest) {

		Account account = accountRepository.findByUsernameAndEmail(forgotPasswordRequest.getUsername(), forgotPasswordRequest.getEmail())
				.orElseThrow(() -> new RuntimeException("Erro: Usuário não encontrado com o username e email informados."));
		
		String token = RandomString.make(30);
		
		account.setResetpasswordtoken(token);
		accountRepository.save(account);
			
		emailUtils.sendEmailForgotPassword(forgotPasswordRequest.getEmail(), token);
		
		return ResponseEntity.ok(new MessageResponse("Enviamos um link de redefinição de senha para o seu e-mail. Por favor, verifique."));
	}

	public ResponseEntity<?> resetpassword(@Valid ResetPasswordRequest resetPasswordRequest) {
		
		Account account = accountRepository.findByResetpasswordtoken(resetPasswordRequest.getToken())
				.orElseThrow(() -> new RuntimeException("Erro: Token inválido."));
		
		String encodedPassword = encoder.encode(resetPasswordRequest.getPassword());
		
		account.setPassword(encodedPassword);
		account.setResetpasswordtoken(null);
		
		accountRepository.save(account);
		
		return ResponseEntity.ok(new MessageResponse("A sua senha foi alterada com sucesso."));
	}
}
