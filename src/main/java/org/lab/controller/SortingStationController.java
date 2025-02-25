package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.annotations.ManagerOnly;
import org.lab.annotations.Secured;
import org.lab.model.Partners;
import org.lab.model.SortingStation;
import org.lab.service.SortingStationService;
import org.lab.utils.PaginatedResponse;
import org.lab.validation.ModelValidator;

import java.util.List;

@Path("/sorting-stations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SortingStationController {

    @Inject
    private SortingStationService sortingStationService;

    @Secured
    @GET
    public Response getAllSortingStations(@QueryParam("page") int page, @QueryParam("size") int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        PaginatedResponse<String> stations = sortingStationService.getAll(page, size);
        return Response.ok(stations).build();
    }

    @Secured
    @GET
    @Path("/{id}")
    public Response getSortingStationById(@PathParam("id") int id) {
        SortingStation station = sortingStationService.getById(id);
        if (station == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(station).build();
    }

    @Secured
    @ManagerOnly
    @POST
    public Response createSortingStation(SortingStation sortingStation) {
        if (!ModelValidator.validate(sortingStation)) {
            return ModelValidator.getValidationErrorResponse();
        }

        sortingStationService.create(sortingStation);
        return Response.status(Response.Status.CREATED).build();
    }

    @Secured
    @ManagerOnly
    @PUT
    @Path("/{id}")
    public Response updateSortingStation(@PathParam("id") int id, SortingStation sortingStation) {
        SortingStation existingSortingStation = sortingStationService.getById(id);
        if (existingSortingStation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        sortingStation.setId(id);

        if (!ModelValidator.validate(sortingStation)) {
            return ModelValidator.getValidationErrorResponse();
        }

        sortingStationService.update(sortingStation);
        return Response.ok(sortingStation).build();
    }


    @Secured
    @ManagerOnly
    @DELETE
    @Path("/{id}")
    public Response deleteSortingStation(@PathParam("id") int id) {
        SortingStation station = sortingStationService.getById(id);
        if (station == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        sortingStationService.delete(station);
        return Response.noContent().build();
    }

    @Secured
    @ManagerOnly
    @POST
    @Path("/{id}/simulate-sort")
    public Response simulateSort(@PathParam("id") int id) {
        SortingStation station = sortingStationService.getById(id);
        if (station == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        sortingStationService.simulateSort(station);
        return Response.ok().build();
    }
}
