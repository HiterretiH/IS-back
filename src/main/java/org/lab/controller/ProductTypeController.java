package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.model.ProductType;
import org.lab.service.ProductTypeService;

import java.util.List;

@Path("/product-types")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductTypeController {

    @Inject
    private ProductTypeService productTypeService;

    @GET
    public Response getAllProductTypes() {
        List<ProductType> productTypes = productTypeService.getAll();
        return Response.ok(productTypes).build();
    }

    @GET
    @Path("/{id}")
    public Response getProductTypeById(@PathParam("id") int id) {
        ProductType productType = productTypeService.getById(id);
        if (productType == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(productType).build();
    }

    @POST
    public Response createProductType(ProductType productType) {
        productTypeService.create(productType);
        return Response.status(Response.Status.CREATED).entity(productType).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProductType(@PathParam("id") int id) {
        ProductType productType = productTypeService.getById(id);
        if (productType == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        productTypeService.delete(productType);
        return Response.noContent().build();
    }
}