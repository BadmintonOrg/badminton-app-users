package si.fri.rso.badmintonappusers.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.badmintonappusers.lib.User;
import si.fri.rso.badmintonappusers.models.converters.UserConverter;
import si.fri.rso.badmintonappusers.models.entities.UsersEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
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

    public User getUser(Integer id) {

        UsersEntity usersEntity = em.find(UsersEntity.class, id);

        if (usersEntity == null) {
            throw new NotFoundException();
        }

        User comm = UserConverter.toDto(usersEntity);

        return comm;
    }

    public User createUser(User comm) {

        UsersEntity usersEntity = UserConverter.toEntity(comm);

        try {
            beginTx();
            em.persist(usersEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (usersEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return UserConverter.toDto(usersEntity);
    }

    public boolean deleteUser(Integer id) {

        UsersEntity comm = em.find(UsersEntity.class, id);

        if (comm != null) {
            try {
                beginTx();
                em.remove(comm);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    public User putUser(Integer id, User comm) {

        UsersEntity c = em.find(UsersEntity.class, id);

        if (c == null) {
            return null;
        }

        UsersEntity updatedUserEntity = UserConverter.toEntity(comm);

        try {
            beginTx();
            updatedUserEntity.setId(c.getId());
            updatedUserEntity = em.merge(updatedUserEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return UserConverter.toDto(updatedUserEntity);
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}