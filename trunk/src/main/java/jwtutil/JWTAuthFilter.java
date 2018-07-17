package jwtutil;

import Service.serviceint.UserService;
import dao.entity.User;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;


@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class JWTAuthFilter implements ContainerRequestFilter {

    @Inject
    UserService userService;

    @Inject
    javax.inject.Provider<UriInfo> uriInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        System.out.println("request filter invoked...");

        String authHeaderVal = requestContext.getHeaderString("Authorization");



        //consume JWT i.e. execute signature validation
        if (authHeaderVal.startsWith("Bearer")) {
            try {
                System.out.println("JWT based Auth in action... time to verify th signature");
                System.out.println("JWT being tested:\n" + authHeaderVal.split(" ")[1]);
                final String subject = validate(authHeaderVal.split(" ")[1]);
                String username = getUsername(authHeaderVal.split(" ")[1]);
                String password = getPassword(authHeaderVal.split(" ")[1]);
                String[] userRole = getRole(authHeaderVal.split(" ")[1]);

                User user = userService.findByUsername(username);
                final SecurityContext securityContext = requestContext.getSecurityContext();
                if(userRole[0]!=null){
                    requestContext.setSecurityContext(new SecurityContextAuthorizer(uriInfo, () -> username, userRole));
                    return;
                }

                    if (username != null &&user != null && user.getPassword().equals(password) ) {
                        if (user.getRole() != null ) {
                            requestContext.setSecurityContext(new SecurityContextAuthorizer(uriInfo, () -> username, userRole));
                            String jwt = JWTokenUtility.buildJWTAuthentication(username, password,user.getRole());


                             requestContext.abortWith(Response.ok(jwt).build());
                        } else {
                            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                            Logger.getLogger("Version or roles did not match the token");
                        }
                    }

            } catch (InvalidJwtException ex) {
                System.out.println("JWT validation failed");

                requestContext.setProperty("auth-failed", true);
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());

            }

        } else {
            System.out.println("No JWT token !");
            requestContext.setProperty("auth-failed", true);
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }

    }

    private String validate(String jwt) throws InvalidJwtException {
        String subject = null;
        RsaJsonWebKey rsaJsonWebKey = RsaKeyProducer.produce();

        System.out.println("RSA hash code... " + rsaJsonWebKey.hashCode());

        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireSubject() // the JWT must have a subject claim
                .setVerificationKey(rsaJsonWebKey.getKey()) // verify the signature with the public key
                .build(); // create the JwtConsumer instance

        try {
            //  Validate the JWT and process it to the Claims
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            subject = (String) jwtClaims.getClaimValue("sub");
            System.out.println("JWT validation succeeded! " + jwtClaims);
        } catch (InvalidJwtException e) {
            e.printStackTrace(); //on purpose
            throw e;
        }

        return subject;
    }

    private String getUsername(String jwt) {
        String username = "";
        try {
            RsaJsonWebKey rsaJsonWebKey = RsaKeyProducer.produce();
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireSubject() // the JWT must have a subject claim
                    .setVerificationKey(rsaJsonWebKey.getKey()) // verify the signature with the public key
                    .build();
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            username = (String) jwtClaims.getClaimValue("username");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return username;
    }

    private String getPassword(String jwt) {
        String password = "";
        try {
            RsaJsonWebKey rsaJsonWebKey = RsaKeyProducer.produce();
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireSubject() // the JWT must have a subject claim
                    .setVerificationKey(rsaJsonWebKey.getKey()) // verify the signature with the public key
                    .build();
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            password = (String) jwtClaims.getClaimValue("password");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    private String[] getRole(String jwt) {
        String[] role = new String[1];
        try {
            RsaJsonWebKey rsaJsonWebKey = RsaKeyProducer.produce();
            JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                    .setRequireSubject() // the JWT must have a subject claim
                    .setVerificationKey(rsaJsonWebKey.getKey()) // verify the signature with the public key
                    .build();
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            role[0] = (String) jwtClaims.getClaimValue("roles");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return role;
    }


}
