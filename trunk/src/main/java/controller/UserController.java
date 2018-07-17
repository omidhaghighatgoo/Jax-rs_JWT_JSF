package controller;


import Service.serviceint.UserService;
import dao.entity.User;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by OmiD.HaghighatgoO on 05/31/2018.
 */



@Path("/api")
/*@Consumes({ "application/json" })
@Produces({ "application/json" })*/
public class UserController {

    @Inject
    UserService userService;

    @Context
    SecurityContext sctx;


    @GET
    @Path("/show")
    @Produces(MediaType.TEXT_PLAIN)
    public String show() {
        return ("salam");
    }

    @GET
    @Path("/hello")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject hello(@QueryParam("name") String name) {
        return Json.createObjectBuilder()
                .add("msg", "Hello World JAX-RS " + name)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/createUser")
    public Response createUser(String userJSon) {

        Jsonb jsonb = JsonbBuilder.create();
        User user = jsonb.fromJson(userJSon, User.class);
        userService.createUser(user);

        userService.loadAll();
        return Response.ok("ok").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin","users"})
    @Path("/loadUsers")
    public Response loadUsers() {
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(userService.loadAll());

        return Response.ok(result).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/loadUserByUsername")
    public Response loadUserByUsername(String username) {
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(userService.findByUsername(username));

        return Response.ok(result).build();
    }


}

