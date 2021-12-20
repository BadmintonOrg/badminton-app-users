package si.fri.rso.badmintonappusers.services.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import si.fri.rso.badmintonappusers.lib.Organization;
import si.fri.rso.badmintonappusers.lib.User;
import si.fri.rso.badmintonappusers.models.converters.UserConverter;
import si.fri.rso.badmintonappusers.models.entities.UsersEntity;
import si.fri.rso.badmintonappusers.services.config.RestProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@RequestScoped
public class UsersBean {

    private Logger log = Logger.getLogger(UsersBean.class.getName());

    @Inject
    private EntityManager em;

    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    @Inject
    @DiscoverService(value = "badmiton-app-organizations-service", version = "1.0.0", environment = "dev")
    private Optional<WebTarget> target;

    @Inject
    private RestProperties restProperties;

    @PostConstruct
    private void init() {
        httpClient = HttpClientBuilder.create().build();
        objectMapper = new ObjectMapper();
    }

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

        log.log(Level.INFO,String.valueOf(restProperties.getUserDiscovery()));
        if(target.isPresent()&&restProperties.getUserDiscovery()){
            Organization org = getOrganizationFromService(id);
            if(org!=null)
                comm.setOrganizationObj(org);
        }

        return comm;
    }

    public Organization getOrganizationFromService(Integer id){
        WebTarget service = target.get().path("v1/organizations");
        log.log(Level.INFO,String.valueOf(service.getUri()));
        try {
            HttpGet request = new HttpGet(service.getUri() + "/" + id);
            HttpResponse response = httpClient.execute(request);
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                if (entity != null)
                    return objectMapper.readValue(EntityUtils.toString(entity), Organization.class);
            } else {
                String msg = "Remote server '"  + "' is responded with status " + status + ".";
                log.log(Level.SEVERE,msg);
                return null;
            }
        } catch (IOException e) {
            log.log(Level.SEVERE,e.getMessage());

            return null;
        }
        return null;
    }


    public Organization getOrgFallback(Integer id) {
        return null;
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