package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.model.Worker;
import org.lab.service.WorkerService;

import java.util.List;

@Path("/workers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WorkerController {

    @Inject
    private WorkerService workerService;

    @GET
    public Response getAllWorkers() {
        List<Worker> workers = workerService.getAll();
        return Response.ok(workers).build();
    }

    @GET
    @Path("/{id}")
    public Response getWorkerById(@PathParam("id") int id) {
        Worker worker = workerService.getById(id);
        if (worker == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(worker).build();
    }

    @POST
    public Response createWorker(Worker worker) {
        workerService.create(worker);
        return Response.status(Response.Status.CREATED).entity(worker).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteWorker(@PathParam("id") int id) {
        Worker worker = workerService.getById(id);
        if (worker == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        workerService.delete(worker);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/fire")
    public Response fireWorker(@PathParam("id") int id) {
        Worker worker = workerService.getById(id);
        if (worker == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        workerService.fire(null);
        return Response.ok().build();
    }
}
