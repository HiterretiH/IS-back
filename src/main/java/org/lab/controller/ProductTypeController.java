package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.annotations.ManagerOnly;
import org.lab.annotations.Secured;
import org.lab.model.ProductType;
import org.lab.service.ProductTypeService;
import org.lab.utils.PaginatedResponse;
import org.lab.validation.ModelValidator;

import java.util.List;

@Path("/product-types")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductTypeController {

    @Inject
    private ProductTypeService productTypeService;

    @Secured
    @GET
    public Response getAllProductTypes(@QueryParam("page") int page, @QueryParam("size") int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        PaginatedResponse<ProductType> productTypes = productTypeService.getAll(page, size);
        return Response.ok(productTypes).build();
    }

    @Secured
    @GET
    @Path("/{id}")
    public Response getProductTypeById(@PathParam("id") int id) {
        ProductType productType = productTypeService.getById(id);
        if (productType == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(productType).build();
    }

    @Secured
    @ManagerOnly
    @POST
    public Response createProductType(ProductType productType) {
        if (!ModelValidator.validate(productType)) {
            return ModelValidator.getValidationErrorResponse();
        }

        productTypeService.create(productType);
        return Response.status(Response.Status.CREATED).entity(productType).build();
    }

    @Secured
    @ManagerOnly
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
