package tk.bolaodacopa.accounts.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import tk.bolaodacopa.accounts.security.services.AccountDetailsImpl;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${bolaodacopa.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	@Autowired
	JwtKeyProvider jwtKeyProvider;

	public String generateJwtToken(Authentication authentication) {
		AccountDetailsImpl accountPrincipal = (AccountDetailsImpl) authentication.getPrincipal();

		return Jwts.builder()
				.setSubject((accountPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(jwtKeyProvider.getPrivateKey(), SignatureAlgorithm.RS256)
				.compact();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder()
			.setSigningKey(jwtKeyProvider.getPublicKey())
			.build()
			.parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}	

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(jwtKeyProvider.getPublicKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
}