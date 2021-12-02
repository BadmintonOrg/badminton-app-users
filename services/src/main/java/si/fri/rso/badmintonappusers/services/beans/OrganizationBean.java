package si.fri.rso.badmintonappusers.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.badmintonappusers.lib.Organization;
import si.fri.rso.badmintonappusers.models.converters.OrganizationConverter;
import si.fri.rso.badmintonappusers.models.entities.OrganizationEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class OrganizationBean {

    private Logger log = Logger.getLogger(si.fri.rso.badmintonappusers.services.beans.OrganizationBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Organization> getOrganizations(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, OrganizationEntity.class, queryParameters).stream()
                .map(OrganizationConverter::toDto).collect(Collectors.toList());
    }

    public Organization getOrganization(Integer id) {

        OrganizationEntity organizationEntity = em.find(OrganizationEntity.class, id);

        if (organizationEntity == null) {
            throw new NotFoundException();
        }

        Organization organization = OrganizationConverter.toDto(organizationEntity);

        return organization;
    }
}
