package tech.itparklessons.fileshares.user.util.mapper;

import org.mapstruct.Mapper;
import tech.itparklessons.fileshares.user.model.dto.RegistrationUserDto;
import tech.itparklessons.fileshares.user.model.dto.UserInformationResponse;
import tech.itparklessons.fileshares.user.model.entity.Users;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users toEntity(RegistrationUserDto accountDto);

    UserInformationResponse toUserInformationResponse(Users users);
}