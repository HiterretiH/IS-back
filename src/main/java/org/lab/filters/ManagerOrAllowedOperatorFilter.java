package org.lab.filters;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.lab.annotations.ManagerOrAllowedOperator;
import org.lab.model.*;
import org.lab.repository.WarehouseOperatorRepository;
import org.lab.service.ProductService;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class ManagerOrAllowedOperatorFilter implements ContainerRequestFilter {

    @Context
    ResourceInfo resourceInfo;

    @Inject
    WarehouseOperatorRepository warehouseOperatorRepository;

    @Inject
    ProductService productService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (resourceInfo.getResourceMethod().isAnnotationPresent(ManagerOrAllowedOperator.class)) {
            User user = (User) requestContext.getProperty("currentUser");

            String path = requestContext.getUriInfo().getPath();
            int productId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));

            Product product = productService.getById(productId);

            if (product == null || product.getProductType() == null) {
                requestContext.abortWith(Response.status(Response.Status.NOT_FOUND).entity("Product not found").build());
                return;
            }

            ProductType productType = product.getProductType();

            WarehouseOperator warehouseOperator = warehouseOperatorRepository.findByUserId(user.getId());

            if (warehouseOperator == null || (warehouseOperator.getProductType() != productType && !user.getRole().equals(Role.MANAGER))) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity("User not authorized to modify this product").build());
            }
        }
    }
}
