package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.model.SortingRequest;
import org.lab.service.SortingRequestService;

import java.util.List;

@Path("/sorting-requests")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SortingRequestController {

    @Inject
    private SortingRequestService sortingRequestService;

    @GET
    public Response getAllSortingRequests() {
        List<SortingRequest> sortingRequests = sortingRequestService.getAll();
        return Response.ok(sortingRequests).build();
    }

    @GET
    @Path("/{id}")
    public Response getSortingRequestById(@PathParam("id") int id) {
        SortingRequest sortingRequest = sortingRequestService.getById(id);
        if (sortingRequest == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(sortingRequest).build();
    }

    @POST
    public Response createSortingRequest(SortingRequest sortingRequest) {
        sortingRequestService.create(sortingRequest);
        return Response.status(Response.Status.CREATED).entity(sortingRequest).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSortingRequest(@PathParam("id") int id) {
        SortingRequest sortingRequest = sortingRequestService.getById(id);
        if (sortingRequest == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        sortingRequestService.delete(sortingRequest);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/reject")
    public Response rejectSortingRequest(@PathParam("id") int id) {
        SortingRequest sortingRequest = sortingRequestService.getById(id);
        if (sortingRequest == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        sortingRequestService.reject(sortingRequest);
        return Response.ok().build();
    }
}
