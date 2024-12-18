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
import org.lab.annotations.AdminOrAllowedOperator;
import org.lab.model.*;
import org.lab.service.ProductService;
import org.lab.service.TypesAndOperatorsService;

import java.lang.annotation.Annotation;
import java.util.List;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class AdminOrAllowedOperatorFilter implements ContainerRequestFilter {

    @Context
    ResourceInfo resourceInfo;

    @Inject
    TypesAndOperatorsService typesAndOperatorsService;

    @Inject
    ProductService productService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        boolean isAdminOrAllowedOperator = false;

        for (Annotation annotation : resourceInfo.getResourceMethod().getAnnotations()) {
            if (AdminOrAllowedOperator.class.isAssignableFrom(annotation.annotationType())) {
                isAdminOrAllowedOperator = true;
                break;
            }
        }

        if (isAdminOrAllowedOperator) {
            User user = (User) requestContext.getProperty("currentUser");

            String path = requestContext.getUriInfo().getPath();
            int productId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));

            Product product = productService.findById(productId);

            if (product == null || product.getProductType() == null) {
                requestContext.abortWith(Response.status(Response.Status.NOT_FOUND).entity("Product not found").build());
                return;
            }

            ProductType productType = product.getProductType();

            List<TypesAndOperators> typesAndOperators = typesAndOperatorsService.findByUserAndType(user, productType);

            if (typesAndOperators.isEmpty() && !user.getRole().equals(Role.ADMIN)) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity("User not authorized to modify this product").build());
            }
        }
    }
}
