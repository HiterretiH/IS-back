package org.lab.filters;
import jakarta.annotation.Priority;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import org.lab.annotations.Secured;
import org.lab.model.Role;
import org.lab.model.User;
import org.lab.utils.JwtUtils;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthFilter implements ContainerRequestFilter {

    @Context
    ResourceInfo resourceInfo;

    @Context
    private HttpServletRequest httpServletRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        if (resourceInfo.getResourceMethod().isAnnotationPresent(Secured.class)) {
            String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            System.out.println("authHeader: " + authHeader);
            if (authHeader == null) {
                requestContext.abortWith(
                        Response.status(Response.Status.UNAUTHORIZED).entity("User not authorized").build()
                );
                return;
            }

            User user = JwtUtils.extractUser(authHeader);

            if (user == null || !JwtUtils.validateToken(authHeader, user)) {
                requestContext.abortWith(
                        Response.status(Response.Status.UNAUTHORIZED).entity("User not authorized").build()
                );
                return;
            }

            httpServletRequest.setAttribute("currentUser", user);
        }
    }
}
