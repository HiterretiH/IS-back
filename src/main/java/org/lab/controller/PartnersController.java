package org.lab.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lab.model.Partners;
import org.lab.service.PartnersService;

import java.util.List;

@Path("/partners")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PartnersController {

    @Inject
    private PartnersService partnersService;

    @GET
    public Response getAllPartners() {
        List<Partners> partners = partnersService.getAll();
        return Response.ok(partners).build();
    }

    @GET
    @Path("/{id}")
    public Response getPartnerById(@PathParam("id") int id) {
        Partners partner = partnersService.getById(id);
        if (partner == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(partner).build();
    }

    @POST
    public Response createPartner(Partners partner) {
        partnersService.create(partner);
        return Response.status(Response.Status.CREATED).entity(partner).build();
    }

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