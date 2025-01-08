package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.model.Queue;
import org.lab.model.SortingStation;
import org.lab.service.QueueService;

import java.util.List;

@Path("/queues")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QueueController {

    @Inject
    private QueueService queueService;

    @GET
    public Response getAllQueues() {
        List<Queue> queues = queueService.getAll();
        return Response.ok(queues).build();
    }

    @GET
    @Path("/{id}")
    public Response getQueueById(@PathParam("id") int id) {
        Queue queue = queueService.getById(id);
        if (queue == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(queue).build();
    }

    @POST
    public Response createQueue(Queue queue) {
        queueService.create(queue);
        return Response.status(Response.Status.CREATED).entity(queue).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteQueue(@PathParam("id") int id) {
        Queue queue = queueService.getById(id);
        if (queue == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        queueService.delete(queue);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/set-sorting-station")
    public Response setSortingStation(@PathParam("id") int id, SortingStation sortingStation) {
        Queue queue = queueService.getById(id);
        if (queue == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        queueService.setSortingStation(sortingStation);
        return Response.ok().build();
    }

    @GET
    @Path("/{id}/products")
    public Response getAllProductsInQueue(@PathParam("id") int id) {
        Queue queue = queueService.getById(id);
        if (queue == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        queueService.getAllProducts(queue);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/interrupt")
    public Response interruptQueue(@PathParam("id") int id) {
        Queue queue = queueService.getById(id);
        if (queue == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        queueService.interrupt(queue);
        return Response.ok().build();
    }
}
