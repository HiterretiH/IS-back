package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.annotations.ManagerOnly;
import org.lab.annotations.Secured;
import org.lab.model.OperatorRequest;
import org.lab.model.RequestState;
import org.lab.model.Role;
import org.lab.model.User;
import org.lab.service.OperatorRequestService;
import org.lab.service.Token;
import org.lab.service.UserService;
import org.lab.validation.ModelValidator;

import java.util.List;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    @Inject
    private UserService userService;

    @Inject
    private OperatorRequestService operatorRequestService;

    @POST
    @Path("/register")
    public Response register(User user) {
        user.setRole(Role.PENDING);

        if (!ModelValidator.validate(user)) {
            return ModelValidator.getValidationErrorResponse();
        }

        if (userService.getByUsername(user.getUsername()) != null) {
            return Response.status(Response.Status.CONFLICT).entity("User with given username already exists").build();
        }

        Token token = userService.register(user);
        if (token.getToken() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Registration failed").build();
        }

        OperatorRequest operatorRequest = new OperatorRequest();
        operatorRequest.setOperator(user);
        operatorRequest.setStatus(RequestState.PENDING);
        operatorRequestService.create(operatorRequest);

        return Response.status(Response.Status.CREATED).entity(token).build();
    }

    @POST
    @Path("/login")
    public Response login(User user) {
        Token token = userService.login(user);
        if (token.getToken() == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
        return Response.ok(token).build();
    }

    @Secured
    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Integer id) {
        User user = userService.getById(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(user).build();
    }

    @Secured
    @GET
    public Response getAllUsers() {
        List<User> users = userService.getAll();
        return Response.ok(users).build();
    }

    @Secured
    @ManagerOnly
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
