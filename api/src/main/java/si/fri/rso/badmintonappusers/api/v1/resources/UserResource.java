package si.fri.rso.badmintonappusers.api.v1.resources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
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

@Log
@ApplicationScoped
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, HEAD, DELETE, OPTIONS, PUT")
public class UserResource {

    private Logger log = Logger.getLogger(UserResource.class.getName());

    @Inject
    private UsersBean usersBean;

    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Get all users in a list", summary = "Get all users")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of users",
                    content = @Content(schema = @Schema(implementation = User.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    public Response getUsers() {

        log.info("Get all users.");
        List<User> user = usersBean.getUsers(uriInfo);

        log.info("Returning users.");
        return Response.status(Response.Status.OK).entity(user).build();
    }

    @Operation(description = "Get data for a user.", summary = "Get data for a user")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "User",
                    content = @Content(
                            schema = @Schema(implementation = User.class))
            )})
    @GET
    @Path("/{userId}")
    public Response getUser(@Parameter(description = "User ID.", required = true)
                                @PathParam("userId") Integer userId) {

        log.info("Get info for user with id " + userId);
        User comm = usersBean.getUser(userId);

        if (comm == null) {
            log.info("No user found.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        log.info("Returning data for user with id " + userId);
        return Response.status(Response.Status.OK).entity(comm).build();
    }

    @Operation(description = "Add user.", summary = "Add user")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "User successfully added."
            ),
            @APIResponse(responseCode = "405", description = "Validation error .")
    })
    @POST
    public Response createUser(@RequestBody(
            description = "DTO object with user data.",
            required = true, content = @Content(
            schema = @Schema(implementation = User.class))) User comm) {

        log.info("Called method for new user");
        if (comm.getOrganization() == null || comm.getName() == null || comm.getPassword() == null || comm.getUserEmail() == null || comm.getSurname() == null) {
            log.info("New user not added. Bad request.");
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            comm = usersBean.createUser(comm);
        }

        log.info("New user added");
        return Response.status(Response.Status.CREATED).entity(comm).build();

    }

    @Operation(description = "Delete user.", summary = "Delete user")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "User successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not found."
            )
    })
    @DELETE
    @Path("{userId}")
    public Response deleteUser(@Parameter(description = "User ID.", required = true)
                                   @PathParam("userId") Integer userId){

        log.info("Called method to delete user");
        boolean deleted = usersBean.deleteUser(userId);

        if (deleted) {
            log.info("User not deleted. Bad request.");
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            log.info("Deleted user with id " + userId);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Operation(description = "Update data for a user.", summary = "Update user")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "User successfully updated."
            )
    })
    @PUT
    @Path("{userId}")
    public Response putUser(@Parameter(description = "User ID.", required = true)
                                @PathParam("userId") Integer userId,
                            @RequestBody(
                                    description = "DTO object with user data.",
                                    required = true, content = @Content(
                                    schema = @Schema(implementation = User.class))) User comm){

        log.info("Called method to update user");
        comm = usersBean.putUser(userId, comm);

        if (comm == null) {
            log.info("User not updated. Bad request.");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        log.info("Updated user with id " + userId);
        return Response.status(Response.Status.NOT_MODIFIED).build();

    }
}