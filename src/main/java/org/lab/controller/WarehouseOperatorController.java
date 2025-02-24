package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.annotations.ManagerOnly;
import org.lab.annotations.Secured;
import org.lab.model.ProductType;
import org.lab.model.WarehouseOperator;
import org.lab.service.WarehouseOperatorService;
import org.lab.validation.ModelValidator;

import java.util.List;

@Path("/warehouse-operators")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WarehouseOperatorController {

    @Inject
    private WarehouseOperatorService warehouseOperatorService;

    @Secured
    @GET
    public Response getAllWarehouseOperators() {
        List<WarehouseOperator> warehouseOperators = warehouseOperatorService.getAll();
        return Response.ok(warehouseOperators).build();
    }

    @Secured
    @GET
    @Path("/{id}")
    public Response getWarehouseOperatorById(@PathParam("id") int id) {
        WarehouseOperator warehouseOperator = warehouseOperatorService.getById(id);
        if (warehouseOperator == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(warehouseOperator).build();
    }

    @Secured
    @ManagerOnly
    @POST
    public Response createWarehouseOperator(WarehouseOperator warehouseOperator) {
        if (!ModelValidator.validate(warehouseOperator)) {
            return ModelValidator.getValidationErrorResponse();
        }

        warehouseOperatorService.create(warehouseOperator);
        return Response.status(Response.Status.CREATED).entity(warehouseOperator).build();
    }

    @Secured
    @ManagerOnly
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

    @Secured
    @ManagerOnly
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
