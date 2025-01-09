package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.annotations.ManagerOnly;
import org.lab.annotations.Secured;
import org.lab.model.Queue;
import org.lab.service.QueueService;
import org.lab.validation.ModelValidator;

import java.util.List;

@Path("/queues")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class QueueController {

    @Inject
    private QueueService queueService;

    @Secured
    @GET
    public Response getAllQueues() {
        List<Queue> queues = queueService.getAll();
        return Response.ok(queues).build();
    }

    @Secured
    @GET
    @Path("/{id}")
    public Response getQueueById(@PathParam("id") int id) {
        Queue queue = queueService.getById(id);
        if (queue == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(queue).build();
    }

    @Secured
    @ManagerOnly
    @POST
    public Response createQueue(Queue queue) {
        if (!ModelValidator.validate(queue)) {
            return ModelValidator.getValidationErrorResponse();
        }

        queueService.create(queue);
        return Response.status(Response.Status.CREATED).entity(queue).build();
    }

    @Secured
    @ManagerOnly
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
}
