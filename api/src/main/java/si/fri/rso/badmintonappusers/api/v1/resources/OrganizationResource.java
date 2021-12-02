package si.fri.rso.badmintonappusers.api.v1.resources;

import si.fri.rso.badmintonappusers.lib.Organization;
import si.fri.rso.badmintonappusers.services.beans.OrganizationBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/organizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganizationResource {

    private Logger log = Logger.getLogger(OrganizationResource.class.getName());

    @Inject
    private OrganizationBean organizationBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getOrganizations() {
        List<Organization> organization = organizationBean.getOrganizations(uriInfo);

        return Response.status(Response.Status.OK).entity(organization).build();
    }

    @GET
    @Path("/{organizationId}")
    public Response getImageMetadata(@PathParam("organizationId") Integer organizationId) {

        Organization organ = organizationBean.getOrganization(organizationId);

        if (organ == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(organ).build();
    }



}
