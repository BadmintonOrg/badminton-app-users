package si.fri.rso.badmintonappusers.models.converters;

import si.fri.rso.badmintonappusers.lib.User;
import si.fri.rso.badmintonappusers.models.entities.UsersEntity;

public class UserConverter {

    public static User toDto(UsersEntity entity) {

        User dto = new User();
        dto.setUserId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setUserEmail(entity.getEmail());
        dto.setPassword(entity.getPassword());
        dto.setOrganization(entity.getOrganizationId());
        return dto;

    }

    public static UsersEntity toEntity(User dto) {

        UsersEntity entity = new UsersEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getUserEmail());
        entity.setPassword(dto.getPassword());
        entity.setId(dto.getUserId());
        entity.setOrganizationId(dto.getOrganization());
        return entity;

    }

}
