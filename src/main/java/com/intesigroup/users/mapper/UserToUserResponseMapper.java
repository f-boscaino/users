package com.intesigroup.users.mapper;
import com.intesigroup.users.entity.User;
import com.intesigroup.users.response.AdminResponse;
import com.intesigroup.users.response.OperatorResponse;
import com.intesigroup.users.response.UserResponse;
import org.mapstruct.*;
import org.mapstruct.factory.*;

@Mapper
public interface UserToUserResponseMapper {
    UserToUserResponseMapper MAPPER = Mappers.getMapper(UserToUserResponseMapper.class);

    UserResponse userToUserResponse(User user);
    OperatorResponse userToOperatorResponse(User user);
    AdminResponse userToAdminResponse(User user);

}
