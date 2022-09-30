package tk.bolaodacopa.accounts.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
public class JwtKeyProvider {


	private final ResourceUtil resourceUtil = new ResourceUtil();
	private final Base64Util base64Util = new Base64Util();

	private PrivateKey privateKey;
	private PublicKey  publicKey;


	public JwtKeyProvider(@Value("${bolaodacopa.path.publicKey}") String pathPublicKey
			, @Value("${bolaodacopa.path.privateKey}") String pathPrivateKey) {
		privateKey = readKey(
				pathPrivateKey,
				this::privateKeySpec,
				this::privateKeyGenerator
				);
	
		publicKey = readKey(
				pathPublicKey,
				this::publicKeySpec,
				this::publicKeyGenerator
				);
	}

	private <T extends Key> T readKey(String resourcePath, Function<String, EncodedKeySpec> keySpec, BiFunction<KeyFactory, EncodedKeySpec, T> keyGenerator) {
		try {
			String keyString = resourceUtil.asString(resourcePath);
			//TODO you can check the headers and throw an exception here if you want

			keyString = keyString.replace("-----BEGIN PRIVATE KEY-----", "");
			keyString = keyString.replace("-----END PRIVATE KEY-----", "");
			keyString = keyString.replace("-----BEGIN PUBLIC KEY-----", "");
			keyString = keyString.replace("-----END PUBLIC KEY-----", "");			
			keyString = keyString.replaceAll("\\s+", "");

			return keyGenerator.apply(KeyFactory.getInstance("RSA"), keySpec.apply(keyString));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private EncodedKeySpec privateKeySpec(String data) {
		return new PKCS8EncodedKeySpec(base64Util.decode(data));
	}

	private EncodedKeySpec publicKeySpec(String data) {
		return new X509EncodedKeySpec(base64Util.decode(data));
	}	

	private PublicKey publicKeyGenerator(KeyFactory kf, EncodedKeySpec spec) {
		try {
			return kf.generatePublic(spec);
		} catch(InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return publicKey;
	}	


	private PrivateKey privateKeyGenerator(KeyFactory kf, EncodedKeySpec spec) {
		try {
			return kf.generatePrivate(spec);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return privateKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}
}
