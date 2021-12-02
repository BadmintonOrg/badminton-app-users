package si.fri.rso.badmintonappusers.api.v1.resources;

import si.fri.rso.badmintonappusers.lib.User;
import si.fri.rso.badmintonappusers.services.beans.UsersBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private Logger log = Logger.getLogger(UserResource.class.getName());

    @Inject
    private UsersBean usersBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getUsers() {

        List<User> user = usersBean.getUsers(uriInfo);

        return Response.status(Response.Status.OK).entity(user).build();
    }
/*
    @GET
    @Path("/{imageMetadataId}")
    public Response getImageMetadata(@PathParam("imageMetadataId") Integer imageMetadataId) {

        ImageMetadata imageMetadata = usersBean.getImageMetadata(imageMetadataId);

        if (imageMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(imageMetadata).build();
    }

    @POST
    public Response createImageMetadata(ImageMetadata imageMetadata) {

        if ((imageMetadata.getTitle() == null || imageMetadata.getDescription() == null || imageMetadata.getUri() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            imageMetadata = usersBean.createImageMetadata(imageMetadata);
        }

        return Response.status(Response.Status.CONFLICT).entity(imageMetadata).build();

    }

    @PUT
    @Path("{imageMetadataId}")
    public Response putImageMetadata(@PathParam("imageMetadataId") Integer imageMetadataId,
                                     ImageMetadata imageMetadata) {

        imageMetadata = usersBean.putImageMetadata(imageMetadataId, imageMetadata);

        if (imageMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

    @DELETE
    @Path("{imageMetadataId}")
    public Response deleteImageMetadata(@PathParam("imageMetadataId") Integer imageMetadataId) {

        boolean deleted = usersBean.deleteImageMetadata(imageMetadataId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
 */
}