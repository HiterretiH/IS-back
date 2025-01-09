package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.model.SortingStation;
import org.lab.service.SortingStationService;

import java.util.List;

@Path("/sorting-stations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SortingStationController {

    @Inject
    private SortingStationService sortingStationService;

    @GET
    public Response getAllSortingStations() {
        List<SortingStation> sortingStations = sortingStationService.getAll();
        return Response.ok(sortingStations).build();
    }

    @GET
    @Path("/{id}")
    public Response getSortingStationById(@PathParam("id") int id) {
        SortingStation sortingStation = sortingStationService.getById(id);
        if (sortingStation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(sortingStation).build();
    }

    @POST
    public Response createSortingStation(SortingStation sortingStation) {
        sortingStationService.create(sortingStation);
        return Response.status(Response.Status.CREATED).entity(sortingStation).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSortingStation(@PathParam("id") int id) {
        SortingStation sortingStation = sortingStationService.getById(id);
        if (sortingStation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        sortingStationService.delete(sortingStation);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Path("/{id}/simulate-sort")
    public Response simulateSort(@PathParam("id") int id) {
        SortingStation sortingStation = sortingStationService.getById(id);
        if (sortingStation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        sortingStationService.simulateSort(sortingStation);
        return Response.ok("Sorting simulation completed for station: " + sortingStation.getId()).build();
    }
}
