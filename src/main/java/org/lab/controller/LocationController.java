package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.annotations.ManagerOnly;
import org.lab.annotations.Secured;
import org.lab.model.Location;
import org.lab.service.LocationService;
import org.lab.utils.PaginatedResponse;
import org.lab.validation.ModelValidator;

@Path("/locations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LocationController {

    @Inject
    private LocationService locationService;

    @Secured
    @GET
    public Response getAllLocations(@QueryParam("page") int page, @QueryParam("size") int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        PaginatedResponse<String> locations = locationService.getAll(page, size);
        return Response.ok(locations).build();
    }

    @Secured
    @GET
    @Path("/{id}")
    public Response getLocationById(@PathParam("id") int id) {
        Location location = locationService.getById(id);
        if (location == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(location).build();
    }

    @Secured
    @ManagerOnly
    @POST
    public Response createLocation(Location location) {
        if (!ModelValidator.validate(location)) {
            return ModelValidator.getValidationErrorResponse();
        }

        locationService.create(location);
        return Response.status(Response.Status.CREATED).entity(location).build();
    }

    @Secured
    @ManagerOnly
    @PUT
    @Path("/{id}")
    public Response updateLocation(@PathParam("id") int id, Location location) {
        Location existingLocation = locationService.getById(id);
        if (existingLocation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        location.setId(id);

        if (!ModelValidator.validate(location)) {
            return ModelValidator.getValidationErrorResponse();
        }

        locationService.update(location);
        return Response.ok(location).build();
    }

    @Secured
    @ManagerOnly
    @DELETE
    @Path("/{id}")
    public Response deleteLocation(@PathParam("id") int id) {
        Location location = locationService.getById(id);
        if (location == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        locationService.delete(location);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
