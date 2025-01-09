package org.lab.filters;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.lab.annotations.ManagerOnly;
import org.lab.model.Role;
import org.lab.model.User;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class ManagerOnlyFilter implements ContainerRequestFilter {

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (resourceInfo.getResourceMethod().isAnnotationPresent(ManagerOnly.class)) {
            User user = (User) requestContext.getProperty("currentUser");

            if (user == null || !user.getRole().equals(Role.MANAGER)) {
                requestContext.abortWith(
                        Response.status(Response.Status.FORBIDDEN).entity("Only managers can access this resource").build()
                );
            }
        }
    }
}
