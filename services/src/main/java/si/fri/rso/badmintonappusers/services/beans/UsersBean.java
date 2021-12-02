package si.fri.rso.badmintonappusers.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.badmintonappusers.lib.User;
import si.fri.rso.badmintonappusers.models.converters.UserConverter;
import si.fri.rso.badmintonappusers.models.entities.UsersEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@RequestScoped
public class UsersBean {

    private Logger log = Logger.getLogger(UsersBean.class.getName());

    @Inject
    private EntityManager em;

    public List<User> getUsers(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, UsersEntity.class, queryParameters).stream()
                .map(UserConverter::toDto).collect(Collectors.toList());
    }

    //TO-DO
}