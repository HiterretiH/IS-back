package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.annotations.ManagerOnly;
import org.lab.annotations.Secured;
import org.lab.model.Queue;
import org.lab.model.Warehouse;
import org.lab.service.WarehouseService;
import org.lab.utils.PaginatedResponse;
import org.lab.validation.ModelValidator;

@Path("/warehouses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WarehouseController {

    @Inject
    private WarehouseService warehouseService;

    @Secured
    @GET
    public Response getAllWarehouses(@QueryParam("page") int page, @QueryParam("size") int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        PaginatedResponse<String> warehouses = warehouseService.getAll(page, size);
        return Response.ok(warehouses).build();
    }

    @Secured
    @GET
    @Path("/{id}")
    public Response getWarehouseById(@PathParam("id") int id) {
        Warehouse warehouse = warehouseService.getById(id);
        if (warehouse == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(warehouse).build();
    }

    @Secured
    @ManagerOnly
    @POST
    public Response createWarehouse(Warehouse warehouse) {
        if (!ModelValidator.validate(warehouse)) {
            return ModelValidator.getValidationErrorResponse();
        }

        warehouseService.create(warehouse);
        return Response.status(Response.Status.CREATED).entity(warehouse).build();
    }

    @Secured
    @ManagerOnly
    @PUT
    @Path("/{id}")
    public Response updateWarehouse(@PathParam("id") int id, Warehouse warehouse) {
        Warehouse existingWarehouse = warehouseService.getById(id);
        if (existingWarehouse == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        warehouse.setId(id);

        if (!ModelValidator.validate(warehouse)) {
            return ModelValidator.getValidationErrorResponse();
        }

        warehouseService.update(warehouse);
        return Response.ok(warehouse).build();
    }

    @Secured
    @ManagerOnly
    @DELETE
    @Path("/{id}")
    public Response deletePartner(@PathParam("id") int id) {
        Warehouse warehouse = warehouseService.getById(id);
        if (warehouse == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        warehouseService.delete(warehouse);
        return Response.noContent().build();
    }
}
