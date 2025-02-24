package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.annotations.ManagerOrAllowedOperator;
import org.lab.annotations.Secured;
import org.lab.model.Product;
import org.lab.model.ProductType;
import org.lab.model.SortingStation;
import org.lab.service.ProductService;
import org.lab.service.SortingStationService;
import org.lab.utils.PaginatedResponse;
import org.lab.validation.ModelValidator;

import java.util.List;

@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    private ProductService productService;

    @Inject
    private SortingStationService sortingStationService;

    @Secured
    @GET
    public Response getAllProducts(@QueryParam("page") int page, @QueryParam("size") int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        PaginatedResponse<String> products = productService.getAll(page, size);
        return Response.ok(products).build();
    }

    @Secured
    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") int id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(product).build();
    }

    @Secured
    @ManagerOrAllowedOperator
    @POST
    public Response createProduct(Product product) {
        if (!ModelValidator.validate(product)) {
            return ModelValidator.getValidationErrorResponse();
        }

        productService.create(product);
        return Response.status(Response.Status.CREATED).entity(product).build();
    }

    @Secured
    @ManagerOrAllowedOperator
    @PUT
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") int id, Product product) {
        Product existingProduct = productService.getById(id);
        if (existingProduct == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        product.setId(id);

        if (!ModelValidator.validate(product)) {
            return ModelValidator.getValidationErrorResponse();
        }

        productService.update(product);
        return Response.ok(product).build();
    }

    @Secured
    @ManagerOrAllowedOperator
    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") int id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        productService.delete(product);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Secured
    @ManagerOrAllowedOperator
    @PUT
    @Path("/{id}/dispose")
    public Response disposeProduct(@PathParam("id") int id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        productService.dispose(product);
        return Response.ok(product).build();
    }

    @Secured
    @ManagerOrAllowedOperator
    @PUT
    @Path("/{id}/sort-to-ship/{stationId}")
    public Response sortToShip(@PathParam("id") int id, @PathParam("stationId") int stationId) {
        Product product = productService.getById(id);
        SortingStation sortingStation = sortingStationService.getById(stationId);
        if (product == null || sortingStation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        productService.sortToShip(product, sortingStation);
        return Response.ok(product).build();
    }

    @Secured
    @ManagerOrAllowedOperator
    @PUT
    @Path("/{id}/sort-to-store/{stationId}")
    public Response sortToStore(@PathParam("id") int id, @PathParam("stationId") int stationId) {
        Product product = productService.getById(id);
        SortingStation sortingStation = sortingStationService.getById(stationId);
        if (product == null || sortingStation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        productService.sortToStore(product, sortingStation);
        return Response.ok(product).build();
    }

    @Secured
    @ManagerOrAllowedOperator
    @PUT
    @Path("/{id}/priority")
    public Response setProductPriority(@PathParam("id") int id, @QueryParam("priority") int priority) {
        Product product = productService.getById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        productService.setPriority(product, priority);
        return Response.ok(product).build();
    }
}
