package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.model.User;
import org.lab.model.WarehouseOperator;
import org.lab.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    private UserService userService;

    @POST
    @Path("/register")
    public Response register(User user) {
        Map<String, String> response = userService.register(user);
        if (response.get("token") == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Registration failed").build();
        }
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @POST
    @Path("/login")
    public Response login(User user) {
        Map<String, String> response = userService.login(user);
        if (response == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Integer id) {
        User user = userService.getById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }

    @GET
    public Response getAllUsers() {
        List<User> users = userService.getAll();
        return Response.ok(users).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Integer id) {
        User user = userService.getById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        userService.delete(id);
        return Response.noContent().build();
    }
}
