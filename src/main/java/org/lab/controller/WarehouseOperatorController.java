package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.model.ProductType;
import org.lab.model.WarehouseOperator;
import org.lab.service.WarehouseOperatorService;

import java.util.List;

@Path("/warehouse-operators")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WarehouseOperatorController {

    @Inject
    private WarehouseOperatorService warehouseOperatorService;

    @GET
    public Response getAllWarehouseOperators() {
        List<WarehouseOperator> warehouseOperators = warehouseOperatorService.getAll();
        return Response.ok(warehouseOperators).build();
    }

    @GET
    @Path("/{id}")
    public Response getWarehouseOperatorById(@PathParam("id") int id) {
        WarehouseOperator warehouseOperator = warehouseOperatorService.getById(id);
        if (warehouseOperator == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(warehouseOperator).build();
    }

    @POST
    public Response createWarehouseOperator(WarehouseOperator warehouseOperator) {
        warehouseOperatorService.create(warehouseOperator);
        return Response.status(Response.Status.CREATED).entity(warehouseOperator).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteWarehouseOperator(@PathParam("id") int id) {
        WarehouseOperator warehouseOperator = warehouseOperatorService.getById(id);
        if (warehouseOperator == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        warehouseOperatorService.delete(warehouseOperator);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @POST
    @Path("/{id}/fire")
    public Response fireWarehouseOperator(@PathParam("id") int id) {
        WarehouseOperator warehouseOperator = warehouseOperatorService.getById(id);
        if (warehouseOperator == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        warehouseOperatorService.fire(warehouseOperator);
        return Response.ok("Warehouse operator fired").build();
    }

    @POST
    @Path("/{id}/assign-product-type")
    public Response assignProductTypeToOperator(@PathParam("id") int id, ProductType productType) {
        WarehouseOperator warehouseOperator = warehouseOperatorService.getById(id);
        if (warehouseOperator == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        warehouseOperatorService.assignProductType(warehouseOperator, productType);
        return Response.ok("Product type assigned to warehouse operator").build();
    }
}
