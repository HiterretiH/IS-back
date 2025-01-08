package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.model.Product;
import org.lab.service.ProductService;

import java.util.List;

@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    private ProductService productService;

    @GET
    public Response getAllProducts() {
        List<Product> products = productService.getAll();
        return Response.ok(products).build();
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") int id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(product).build();
    }

    @POST
    public Response createProduct(Product product) {
        productService.create(product);
        return Response.status(Response.Status.CREATED).entity(product).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") int id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        productService.delete(product);
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}/ship")
    public Response shipProduct(@PathParam("id") int id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        productService.ship(product);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/dispose")
    public Response disposeProduct(@PathParam("id") int id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        productService.dispose(product);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/sort-to-store")
    public Response sortProductToStore(@PathParam("id") int id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        productService.sortToStore(product);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/sort-to-ship")
    public Response sortProductToShip(@PathParam("id") int id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        productService.sortToShip(product);
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/set-priority")
    public Response setProductPriority(@PathParam("id") int id, Product product) {
        Product existingProduct = productService.getById(id);
        if (existingProduct == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        productService.setPriority(product);
        return Response.ok().build();
    }
}
