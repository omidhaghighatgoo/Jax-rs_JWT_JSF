package controller;

import Service.serviceint.UserService;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import dao.entity.User;
import jwtutil.JWTokenUtility;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.net.URI;

/**
 * just a dummy resource used to return the JWT token in response header
 */
@Path("/api")
@Stateless
@SecurityDomain("other")
public class AuthController {

    @Context
    SecurityContext sctx;

    @Inject
    UserService userService;

    @GET
    @Path("/auth")
    @RolesAllowed("admin")
    @Produces("text/plain")
    public Response auth() {
        System.out.println("Authenticated user: " + sctx.getUserPrincipal().getName());

        //this.sctx = sctx;
        String authenticatedUser = sctx.getUserPrincipal().getName();
        User user = userService.findByUsername(authenticatedUser);

        String sa = sctx.getAuthenticationScheme();



        /*Response resp = Response.ok(authenticatedUser + " authenticated")
                .header("jwt", JWTokenUtility.buildJWT(authenticatedUser))
                .build();
*/
        Response.ResponseBuilder builder;
        try {
            // builder=Response.seeOther(new URI("/home.xhtml"));
            builder = Response.temporaryRedirect(new URI("../pages/home.xhtml"));
            return builder.build();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }


    }

}
