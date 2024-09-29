package grupo11.megastore.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JWTUtil {

	@Value("${jwt_secret}")
	private String secret;

	@Value("${jwt.token.validity}")
	private long jwtTokenValidity;

	public String generateToken(String email) throws IllegalArgumentException, JWTCreationException {
		Date issuedAt = new Date();
		Date expirationDate = new Date(issuedAt.getTime() + jwtTokenValidity * 1000);

		return JWT.create()
				.withSubject("User Details")
				.withClaim("email", email)
				.withIssuedAt(issuedAt)
				.withExpiresAt(expirationDate)
				.withIssuer("Event Scheduler")
				.sign(Algorithm.HMAC256(secret));
	}

	public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException {
		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
				.withSubject("User Details")
				.withIssuer("Event Scheduler")
				.build();

		DecodedJWT jwt = verifier.verify(token);

		return jwt.getClaim("email").asString();
	}
}