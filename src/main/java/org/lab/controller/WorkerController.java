package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.annotations.ManagerOnly;
import org.lab.annotations.Secured;
import org.lab.model.Status;
import org.lab.model.Worker;
import org.lab.service.WorkerService;
import org.lab.utils.PaginatedResponse;
import org.lab.validation.ModelValidator;

import java.util.List;

@Path("/workers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WorkerController {

    @Inject
    private WorkerService workerService;

    @Secured
    @GET
    public Response getAllWorkers(@QueryParam("page") int page, @QueryParam("size") int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        PaginatedResponse<String> workers = workerService.getAll(page, size);
        return Response.ok(workers).build();
    }

    @GET
    @Path("/{id}")
    @Secured
    public Response getWorkerById(@PathParam("id") int id) {
        Worker worker = workerService.getById(id);
        if (worker == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(worker).build();
    }

    @POST
    @Secured
    @ManagerOnly
    public Response createWorker(Worker worker) {
        worker.setStatus(Status.PENDING);

        if (!ModelValidator.validate(worker)) {
            return ModelValidator.getValidationErrorResponse();
        }

        workerService.create(worker);
        return Response.status(Response.Status.CREATED).entity(worker).build();
    }

    @DELETE
    @Path("/{id}")
    @Secured
    @ManagerOnly
    public Response deleteWorker(@PathParam("id") int id) {
        Worker worker = workerService.getById(id);
        if (worker == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        workerService.delete(worker);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Secured
    @ManagerOnly
    @POST
    @Path("/{id}/hire")
    public Response hireWorker(@PathParam("id") int id) {
        Worker worker = workerService.getById(id);
        if (worker == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        workerService.hire(worker);
        return Response.ok("Worker hired").build();
    }

    @Secured
    @ManagerOnly
    @POST
    @Path("/{id}/fire")
    public Response fireWorker(@PathParam("id") int id) {
        Worker worker = workerService.getById(id);
        if (worker == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        workerService.fire(worker);
        return Response.ok("Worker fired").build();
    }

    @Secured
    @ManagerOnly
    @POST
    @Path("/{id}/reject")
    public Response rejectWorker(@PathParam("id") int id) {
        Worker worker = workerService.getById(id);
        if (worker == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        workerService.reject(worker);
        return Response.ok("Worker rejected").build();
    }
}
