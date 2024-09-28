package grupo11.megastore.users.dto;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import grupo11.megastore.users.dto.user.RegisterUserDTO;
import grupo11.megastore.users.dto.user.UserDTO;
import grupo11.megastore.users.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IUserMapper {
    UserDTO userToUserDTO(User user);

    User userDTOToUser(UserDTO userDTO);

    User registerUserDTOToUser(RegisterUserDTO registerUserDTO);
}
