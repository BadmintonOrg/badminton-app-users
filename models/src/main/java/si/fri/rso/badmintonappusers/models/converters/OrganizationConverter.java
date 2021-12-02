package si.fri.rso.badmintonappusers.models.converters;

import si.fri.rso.badmintonappusers.models.entities.OrganizationEntity;
import si.fri.rso.badmintonappusers.lib.Organization;

public class OrganizationConverter {

    public static Organization toDto(OrganizationEntity entity) {

        Organization dto = new Organization();
        dto.setOrganizationId(entity.getId());
        dto.setName(entity.getName());

        return dto;

    }

    public static OrganizationEntity toEntity(Organization dto) {

        OrganizationEntity entity = new OrganizationEntity();
        entity.setName(dto.getName());
        entity.setId(dto.getOrganizationId());
        return entity;

    }

}
