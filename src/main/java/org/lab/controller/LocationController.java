package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.model.Location;
import org.lab.service.LocationService;

import java.util.List;

@Path("/locations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LocationController {

    @Inject
    private LocationService locationService;

    @GET
    public Response getAllLocations() {
        List<Location> locations = locationService.getAll();
        return Response.ok(locations).build();
    }

    @GET
    @Path("/{id}")
    public Response getLocationById(@PathParam("id") int id) {
        Location location = locationService.getById(id);
        if (location == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(location).build();
    }

    @POST
    public Response createLocation(Location location) {
        locationService.create(location);
        return Response.status(Response.Status.CREATED).entity(location).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateLocation(@PathParam("id") int id, Location location) {
        Location existingLocation = locationService.getById(id);
        if (existingLocation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        location.setId(id); // Ensure the correct ID is set
        locationService.update(location);
        return Response.ok(location).build();
    }

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
