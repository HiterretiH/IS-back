package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.model.OperatorRequest;
import org.lab.model.User;
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
    @Path("/mine/{id}")
    public Response getAllOperatorRequestsByUserId(@PathParam("id") int id) {
        List<OperatorRequest> operatorRequests = operatorRequestService.getAllByUserId(id);
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
    @Path("/{id}")
    public Response createOperatorRequest(@PathParam("id") int id) {
        OperatorRequest operatorRequest = operatorRequestService.create(id);
        System.out.println(operatorRequest);
        return Response.status(Response.Status.CREATED).entity(operatorRequest).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateOperatorRequest(@PathParam("id") int id, OperatorRequest operatorRequest) {
        OperatorRequest existingRequest = operatorRequestService.getById(id);
        if (existingRequest == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        operatorRequest.setId(id);
        operatorRequestService.update(operatorRequest);
        return Response.ok(operatorRequest).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOperatorRequest(@PathParam("id") int id) {
        OperatorRequest operatorRequest = operatorRequestService.getById(id);
        if (operatorRequest == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        operatorRequestService.delete(operatorRequest);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/pending")
    public Response getAllPendingOperatorRequests() {
        List<OperatorRequest> pendingRequests = operatorRequestService.getAllPending();
        return Response.ok(pendingRequests).build();
    }

    @PUT
    @Path("/{id}/approve")
    public Response approveOperatorRequest(@PathParam("id") int id) {
        OperatorRequest operatorRequest = operatorRequestService.getById(id);
        if (operatorRequest == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        operatorRequestService.approve(operatorRequest);
        return Response.ok(operatorRequest).build();
    }

    @PUT
    @Path("/{id}/reject")
    public Response rejectOperatorRequest(@PathParam("id") int id) {
        OperatorRequest operatorRequest = operatorRequestService.getById(id);
        if (operatorRequest == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        operatorRequestService.reject(operatorRequest);
        return Response.ok(operatorRequest).build();
    }
}
