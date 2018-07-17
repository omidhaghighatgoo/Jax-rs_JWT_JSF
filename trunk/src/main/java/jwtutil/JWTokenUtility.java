package jwtutil;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JWTokenUtility {

    public static String buildJWT(String username , String password ) {
        RsaJsonWebKey rsaJsonWebKey = RsaKeyProducer.produce();
        System.out.println("RSA hash code... " + rsaJsonWebKey.hashCode() + rsaJsonWebKey.getPrivateKey());

        JwtClaims claims = new JwtClaims();
        claims.setSubject("Authentication"); // the subject/principal is whom the token is about

        claims.setClaim("username" , username);
        claims.setClaim("password" , password);

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        String jwt = null;
        try {
            jwt = jws.getCompactSerialization();
        } catch (JoseException ex) {
            Logger.getLogger(JWTAuthFilter.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Claim:\n" + claims);
        System.out.println("JWS:\n" + jws);
        System.out.println("JWT:\n" + jwt);

        return jwt;
    }

    public static String buildJWTAuthentication(String username , String password ,String roles ) {
        RsaJsonWebKey rsaJsonWebKey = RsaKeyProducer.produce();
        System.out.println("RSA hash code... " + rsaJsonWebKey.hashCode() + rsaJsonWebKey.getPrivateKey());

        JwtClaims claims = new JwtClaims();
        claims.setSubject("Authentication"); // the subject/principal is whom the token is about

        claims.setClaim("username" , username);
        claims.setClaim("password" , password);
        claims.setClaim("roles" , roles);


        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);


        String jwt = null;
        try {
            jwt = jws.getCompactSerialization();
        } catch (JoseException ex) {
            Logger.getLogger(JWTAuthFilter.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Claim:\n" + claims);
        System.out.println("JWS:\n" + jws);
        System.out.println("JWT:\n" + jwt);

        return jwt;
    }
}
