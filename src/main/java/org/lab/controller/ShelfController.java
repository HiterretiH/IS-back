package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.annotations.ManagerOnly;
import org.lab.annotations.Secured;
import org.lab.model.Shelf;
import org.lab.service.ShelfService;

import java.util.List;

@Path("/shelves")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ShelfController {

    @Inject
    private ShelfService shelfService;

    @Secured
    @GET
    public Response getAllShelves() {
        List<Shelf> shelves = shelfService.getAll();
        return Response.ok(shelves).build();
    }

    @Secured
    @GET
    @Path("/{id}")
    public Response getShelfById(@PathParam("id") int id) {
        Shelf shelf = shelfService.getById(id);
        if (shelf == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(shelf).build();
    }

    @Secured
    @ManagerOnly
    @POST
    public Response createShelf(Shelf shelf) {
        shelfService.create(shelf);
        return Response.status(Response.Status.CREATED).entity(shelf).build();
    }

    @Secured
    @ManagerOnly
    @DELETE
    @Path("/{id}")
    public Response deleteShelf(@PathParam("id") int id) {
        Shelf shelf = shelfService.getById(id);
        if (shelf == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        shelfService.delete(shelf);
        return Response.noContent().build();
    }
}
