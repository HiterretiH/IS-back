package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.annotations.ManagerOnly;
import org.lab.annotations.Secured;
import org.lab.model.Location;
import org.lab.model.Partners;
import org.lab.service.PartnersService;
import org.lab.utils.PaginatedResponse;
import org.lab.validation.ModelValidator;

import java.util.List;

@Path("/partners")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PartnersController {

    @Inject
    private PartnersService partnersService;

    @Secured
    @GET
    public Response getAllPartners(@QueryParam("page") int page, @QueryParam("size") int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        PaginatedResponse<String> partners = partnersService.getAll(page, size);
        return Response.ok(partners).build();
    }

    @Secured
    @GET
    @Path("/{id}")
    public Response getPartnerById(@PathParam("id") int id) {
        Partners partner = partnersService.getById(id);
        if (partner == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(partner).build();
    }

    @Secured
    @ManagerOnly
    @POST
    public Response createPartner(Partners partner) {
        if (!ModelValidator.validate(partner)) {
            return ModelValidator.getValidationErrorResponse();
        }

        partnersService.create(partner);
        return Response.status(Response.Status.CREATED).entity(partner).build();
    }

    @Secured
    @ManagerOnly
    @PUT
    @Path("/{id}")
    public Response updatePartners(@PathParam("id") int id, Partners partners) {
        Partners existingPartners = partnersService.getById(id);
        if (existingPartners == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        partners.setId(id);

        if (!ModelValidator.validate(partners)) {
            return ModelValidator.getValidationErrorResponse();
        }

        partnersService.update(partners);
        return Response.ok(partners).build();
    }

    @Secured
    @ManagerOnly
    @DELETE
    @Path("/{id}")
    public Response deletePartner(@PathParam("id") int id) {
        Partners partner = partnersService.getById(id);
        if (partner == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        partnersService.delete(partner);
        return Response.noContent().build();
    }
}
