package si.fri.rso.badmintonappusers.api.v1.resources;

import si.fri.rso.badmintonappusers.lib.User;
import si.fri.rso.badmintonappusers.services.beans.UsersBean;

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

    @GET
    @Path("/{userId}")
    public Response getUser(@PathParam("userId") Integer userId) {

        User comm = usersBean.getUser(userId);

        if (comm == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(comm).build();
    }

    @POST
    public Response createUser(User comm) {


        if (comm.getOrganization() == null || comm.getName() == null || comm.getPassword() == null || comm.getUserEmail() == null || comm.getSurname() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            comm = usersBean.createUser(comm);
        }

        return Response.status(Response.Status.CREATED).entity(comm).build();

    }

    @DELETE
    @Path("{userId}")
    public Response deleteUser(@PathParam("userId") Integer userId){

        boolean deleted = usersBean.deleteUser(userId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("{userId}")
    public Response putUser(@PathParam("userId") Integer userId,
                               User comm){

        comm = usersBean.putUser(userId, comm);

        if (comm == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }
}