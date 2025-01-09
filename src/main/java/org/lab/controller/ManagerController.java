package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.annotations.ManagerOnly;
import org.lab.annotations.Secured;
import org.lab.model.Manager;
import org.lab.model.User;
import org.lab.service.ManagerService;

import java.util.List;

@Path("/managers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ManagerController {

    @Inject
    private ManagerService managerService;

    @Secured
    @GET
    public Response getAllManagers() {
        List<Manager> managers = managerService.getAll();
        return Response.ok(managers).build();
    }

    @Secured
    @GET
    @Path("/{id}")
    public Response getManagerById(@PathParam("id") int id) {
        Manager manager = managerService.getById(id);
        if (manager == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(manager).build();
    }

    @Secured
    @ManagerOnly
    @POST
    public Response createManager(Manager manager) {
        managerService.create(manager);
        return Response.status(Response.Status.CREATED).entity(manager).build();
    }

    @Secured
    @ManagerOnly
    @PUT
    @Path("/{id}")
    public Response updateManager(@PathParam("id") int id, Manager manager) {
        Manager existingManager = managerService.getById(id);
        if (existingManager == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        manager.setId(id); // Ensure the correct ID is set
        managerService.update(manager);
        return Response.ok(manager).build();
    }

    @Secured
    @ManagerOnly
    @DELETE
    @Path("/{id}")
    public Response deleteManager(@PathParam("id") int id) {
        Manager manager = managerService.getById(id);
        if (manager == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        managerService.delete(manager);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Secured
    @ManagerOnly
    @PUT
    @Path("/{id}/reassign")
    public Response reassignManager(@PathParam("id") int id, User user) {
        Manager manager = managerService.getById(id);
        if (manager == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        managerService.reassign(manager, user);
        return Response.ok(manager).build();
    }
}
