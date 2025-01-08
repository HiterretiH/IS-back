package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.model.OperatorRequest;
import org.lab.service.OperatorRequestService;

import java.util.List;

@Path("/operator-requests")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OperatorRequestController {

    @Inject
    private OperatorRequestService operatorRequestService;

    @GET
    public Response getAllOperatorRequests() {
        List<OperatorRequest> operatorRequests = operatorRequestService.getAll();
        return Response.ok(operatorRequests).build();
    }

    @GET
    @Path("/{id}")
    public Response getOperatorRequestById(@PathParam("id") int id) {
        OperatorRequest operatorRequest = operatorRequestService.getById(id);
        if (operatorRequest == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(operatorRequest).build();
    }

    @POST
    public Response createOperatorRequest(OperatorRequest operatorRequest) {
        operatorRequestService.create(operatorRequest);
        return Response.status(Response.Status.CREATED).entity(operatorRequest).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOperatorRequest(@PathParam("id") int id) {
        OperatorRequest operatorRequest = operatorRequestService.getById(id);
        if (operatorRequest == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        operatorRequestService.delete(operatorRequest);
        return Response.noContent().build();
    }

    @GET
    @Path("/pending")
    public Response getAllPendingOperatorRequests() {
        operatorRequestService.getAllPending();
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/approve")
    public Response approveOperatorRequest(@PathParam("id") int id) {
        OperatorRequest operatorRequest = operatorRequestService.getById(id);
        if (operatorRequest == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        operatorRequestService.approve(operatorRequest);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/reject")
    public Response rejectOperatorRequest(@PathParam("id") int id) {
        OperatorRequest operatorRequest = operatorRequestService.getById(id);
        if (operatorRequest == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        operatorRequestService.reject(operatorRequest);
        return Response.ok().build();
    }
}
